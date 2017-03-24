/*
 * Copyright 2016 Dennis Vriend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dnvriend

import org.slf4j.{ Logger, LoggerFactory, MDC }

import scala.concurrent.Future

class MDCFuturesTest extends TestSpec {
  val xs: List[(Int, String)] = (0 to 4).zip(('a' to 'e').map(_.toString)).toList
  val logger: Logger = LoggerFactory.getLogger(getClass)
  def computeAsyncA(a: Int): Future[Int] = Future {
    val name = Thread.currentThread().getName
    val mdcText = MDC.get("id")
    xs(a)._2 shouldBe mdcText
    logger.info(s"AsyncA: Computing numbers: ($a - $mdcText - $name)")
    a
  }

  def computeAsyncB(a: Int): Future[Int] = Future {
    val name = Thread.currentThread().getName
    val mdcText = MDC.get("id")
    xs(a)._2 shouldBe mdcText
    logger.info(s"AsyncB: Computing numbers: ($a - $mdcText - $name)")
    a
  }

  def doMdc(a: Int, mdcText: String): Future[Int] = Future {
    MDC.putCloseable("id", mdcText)
    val name = Thread.currentThread().getName
    logger.info(s"doMdc: Computing numbers: ($a - $mdcText - $name)")
    a
  }

  def doCompose(x: Int, mdcMessage: String): Future[Int] = (for {
    a <- doMdc(x, mdcMessage)
    c <- computeAsyncA(x)
    d <- computeAsyncB(x)
  } yield d).map { total =>
    val name = Thread.currentThread().getName
    logger.debug(s"Total computation: $name")
    total
  }

  it should "executeMdc (0, 'a')" in {
    doCompose(0, "a").futureValue shouldBe 0
  }

  it should "execute multiple futures with correct message on MDC" in {
    Future.sequence(xs.map {
      case (x, mdc) => doCompose(x, mdc)
    }).futureValue.sorted shouldBe List(0, 1, 2, 3, 4)
  }

  it should "log also from threadlocal" in {
    MDC.put("id", "54321")
    logger.info("Hi there")
    Future.sequence(List(1, 2, 3, 4, 5).map(_ => Future(logger.info("From future: " + Thread.currentThread().getName)))).map(_ => ()).futureValue shouldBe ()
    logger.info("Done")
  }
}
