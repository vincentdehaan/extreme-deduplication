package nl.vindh.extdup.openapi

import nl.vindh.extdup.api.Api
import sttp.apispec.openapi.Server

import java.io.{File, PrintWriter}
import sttp.apispec.openapi.circe.yaml._
import sttp.tapir.AnyEndpoint
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter

object DocWriter extends App with OpenAPIDocsInterpreter {
  val endpointList: List[AnyEndpoint] = new Api(null).endpoints.map(_.endpoint)
  val docs = toOpenAPI(endpointList, "My App", "1.0")
    .copy(servers = List(Server("http://localhost:8080")))
  val filePath = new File("target/openapi/openapi.yaml")
  filePath.getParentFile.mkdirs()
  val printWriter = new PrintWriter(filePath)
  printWriter.print(docs.toYaml)
  printWriter.close()
}
