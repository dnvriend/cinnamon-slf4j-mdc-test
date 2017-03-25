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

package com.github.dnvriend.adapters.repository

import java.util.UUID

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ ExecutionContext, Future }

class PersonRepository(implicit ec: ExecutionContext) extends LazyLogging {
  def randomId(): String = UUID.randomUUID.toString
  def generatePersonId(): Future[String] = Future {
    val id = randomId()
    logger.debug(s"Generating person id: $id")
    id
  }

  def getPersonName(id: String, name: String): Future[String] = Future {
    logger.debug("Getting person name")
    s"id='$id', name='$name'"
  }
}
