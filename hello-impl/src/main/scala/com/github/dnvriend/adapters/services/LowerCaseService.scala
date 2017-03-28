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

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.event.LoggingReceive
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ ExecutionContext, Future }
import akka.pattern.ask

trait LowerCaseService {
  def toLowerCase(msg: String): Future[String]
}

class ActorLowerCaseService(implicit system: ActorSystem, ec: ExecutionContext, timeout: Timeout) extends LowerCaseService with LazyLogging {
  val lowerCaseActor: ActorRef = system.actorOf(Props(classOf[LowerCaseActor]))

  override def toLowerCase(msg: String): Future[String] = {
    logger.debug(s"ToLowerCase: $msg")
    (lowerCaseActor ? msg).mapTo[String]
  }
}

class LowerCaseActor extends Actor with ActorLogging {
  override def receive: Receive = LoggingReceive {
    case msg: String =>
      log.debug(s"lowering the case of $msg")
      sender ! msg.toLowerCase()
    case ignore =>
      log.debug(s"Ignoring: $ignore")
  }
}
