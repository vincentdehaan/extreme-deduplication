package nl.vindh.extdup.openapi

import nl.vindh.extdup.api.Api
import sttp.apispec.openapi.Server
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.AnyEndpoint

trait Docs extends OpenAPIDocsInterpreter {
  val endpointList: List[AnyEndpoint] = new Api(null).endpoints.toList.map(_.endpoint)

  val docs = toOpenAPI(endpointList, "My App", "1.0").copy(servers = List(Server("http://localhost:8080")))
}
