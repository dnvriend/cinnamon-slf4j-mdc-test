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

import akka.Done
import akka.actor.{ Actor, ActorLogging, ActorRef, Props }
import akka.testkit.TestProbe
import org.slf4j.MDC

case class DoPing(mdcText: String)
case class Ping(mdcText: String)
case class Pong(mdcText: String)

class PingActor(pong: ActorRef, tp: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case DoPing(mdcText) =>
      MDC.put("id", mdcText)
      val name = Thread.currentThread().getName
      log.info("Received DoPing: {}", name)
      pong ! Ping(mdcText)
    case Pong(mdcText) =>
      val name = Thread.currentThread().getName
      log.info("Received Pong: {}", name)
      require(MDC.get("id") == mdcText)
      MDC.remove("id")
      tp ! Done
  }
}

class PongActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case Ping(mdcText) =>
      require(MDC.get("id") == mdcText)
      val name = Thread.currentThread().getName
      log.info("Received ping: {}", name)
      sender() ! Pong(mdcText)
  }
}

class MDCActorTest extends TestSpec {
  def withPingPong(f: TestProbe => ActorRef => Unit): Unit = {
    val tp = TestProbe()
    val pong = system.actorOf(Props(new PongActor))
    val ping = system.actorOf(Props(new PingActor(pong, tp.ref)))
    try f(tp)(ping) finally killActors(ping, pong)
  }

  it should "send with the correct MDC id message" in withPingPong { tp => ref =>
    tp.send(ref, DoPing("abcde"))
    tp.expectMsg(Done)
    tp.send(ref, DoPing("efghi"))
    tp.expectMsg(Done)
  }
}
