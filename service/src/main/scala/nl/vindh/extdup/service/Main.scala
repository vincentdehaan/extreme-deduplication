package nl.vindh.extdup.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import nl.vindh.extdup.api.Api
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

import scala.io.StdIn

object Main extends App {
  implicit val system = ActorSystem("my-system")
  implicit val executionContext = system.dispatcher
  implicit val dynamoDbClient: DynamoDbClient = ???

  val fooRepo = new FooRepository()
  val service = new ServiceImpl(fooRepo)
  val api = new Api(service)

  val bindingFuture = Http().newServerAt("0.0.0.0", 8080).bind(
    AkkaHttpServerInterpreter().toRoute(api.endpoints))

  StdIn.readLine()
}
