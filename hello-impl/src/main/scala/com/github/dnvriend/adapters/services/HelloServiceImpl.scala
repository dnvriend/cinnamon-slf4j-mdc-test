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

package com.github.dnvriend.adapters.services

import com.github.dnvriend.adapters.repository.PersonRepository
import com.github.dnvriend.api.HelloService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.typesafe.scalalogging.LazyLogging
import org.slf4j.MDC

import scala.concurrent.ExecutionContext

class HelloServiceImpl(personRepo: PersonRepository, upperCaseService: UpperCaseService)(implicit ec: ExecutionContext) extends HelloService with LazyLogging {

  override def hello(name: String) = ServiceCall { _ =>
    val id: String = personRepo.randomId()
    MDC.put("id", id)
    (for {
      name <- personRepo.getPersonName(id, id)
      upper <- upperCaseService.toUpperCase(name)
    } yield upper).map { result =>
      logger.debug(s"Hello $id - $result")
      MDC.clear()
      result
    }
  }
}
