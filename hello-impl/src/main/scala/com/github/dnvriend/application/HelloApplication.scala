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

package com.github.dnvriend.application

import akka.actor.ActorSystem
import akka.util.Timeout
import com.github.dnvriend.adapters.repository.PersonRepository
import com.github.dnvriend.adapters.services.{ ActorLowerCaseService, HelloServiceImpl, LowerCaseService, UpperCaseService }
import com.github.dnvriend.api.HelloService
import com.lightbend.lagom.scaladsl.server.{ LagomApplication, LagomApplicationContext, LagomServer }
import com.softwaremill.macwire.wire
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.ws.ahc.AhcWSComponents
import scala.concurrent.duration._

abstract class HelloApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with AhcWSComponents with LazyLogging {

  logger.debug("Launching...")

  implicit val timeout: Timeout = 10.seconds
  implicit val system: ActorSystem = actorSystem
  lazy val personRepo: PersonRepository = wire[PersonRepository]
  lazy val upperCaseService: UpperCaseService = wire[UpperCaseService]
  lazy val lowerCaseService: LowerCaseService = wire[ActorLowerCaseService]

  override lazy val lagomServer: LagomServer = LagomServer.forServices(
    bindService[HelloService].to(wire[HelloServiceImpl])
  )
}
