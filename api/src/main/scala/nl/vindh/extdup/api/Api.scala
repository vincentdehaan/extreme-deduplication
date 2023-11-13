package nl.vindh.extdup.api

import nl.vindh.extdup.domain._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import io.circe.generic.auto._
import sttp.model.StatusCode

class Api(service: Service) {

  val endpoints = List(
    endpoint
      .post
      .in("foo")
      .in(jsonBody[Foo])
      .out(statusCode(StatusCode.Accepted))
      .description("Use this endpoint to create a foo")
      .serverLogic(service.createFoo),

    endpoint
      .get
      .in("foo")
      .in(query[String]("name"))
      .out(jsonBody[Foo])
      .description("Use this endpoint to retrieve a foo")
      .serverLogic(service.getFooByName)
  )
}
