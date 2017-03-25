package com.github.dnvriend.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{ Service, ServiceCall }
import com.lightbend.lagom.scaladsl.api.Service._
import play.api.libs.json.{ Format, Json }

trait HelloService extends Service {
  def hello(name: String): ServiceCall[NotUsed, String]

  override final def descriptor = {
    named("hello").withCalls(
      pathCall("/api/hello/:name", hello _)
    ).withAutoAcl(true)
  }
}

case class GreetingMessage(message: String)

object GreetingMessage {
  implicit val format: Format[GreetingMessage] = Json.format
}
