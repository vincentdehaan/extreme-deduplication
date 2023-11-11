package nl.vindh.extdup.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import nl.vindh.extdup.api.Api
import nl.vindh.extdup.domain.Foo
import org.scanamo.LocalDynamoDB
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, EnvironmentVariableCredentialsProvider, StaticCredentialsProvider}
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

import java.net.URI
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.io.StdIn

object Main extends App {
  implicit val system = ActorSystem("my-system")
  implicit val executionContext = system.dispatcher
  implicit val dynamoDbClient: DynamoDbClient =     DynamoDbClient.builder
    .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
    .endpointOverride(URI.create(s"http://localhost:4566"))
    .region(Region.EU_CENTRAL_1)
    .build

  val fooRepo = new FooRepository()
  val service = new ServiceImpl(fooRepo)
  val api = new Api(service)

  val bindingFuture = Http().newServerAt("0.0.0.0", 8080).bind(
    AkkaHttpServerInterpreter().toRoute(api.endpoints))

  Future {
    println("BLA")
    Thread.sleep(1000)
    println("VBLI")
    val x = for {
      r <- fooRepo.put(Foo("ha", 1, "ho"))
      _ = println(r)
      rr <- fooRepo.getByName("ha")
      _ = println(rr)
    } yield ()
    x.recover {
      case ex => println(ex); throw ex
    }
    println(":BELw")
  }

  StdIn.readLine()
}
