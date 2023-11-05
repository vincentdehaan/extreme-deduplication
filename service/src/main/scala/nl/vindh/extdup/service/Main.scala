package nl.vindh.extdup.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import nl.vindh.extdup.api.Api

import scala.io.StdIn

object Main extends App {
  implicit val system = ActorSystem("my-system")
  implicit val executionContext = system.dispatcher

  val service = new ServiceImpl()
  val api = new Api(service)

  val bindingFuture = Http().newServerAt("0.0.0.0", 8080).bind(
    AkkaHttpServerInterpreter().toRoute(api.endpoints))

  StdIn.readLine()
}
