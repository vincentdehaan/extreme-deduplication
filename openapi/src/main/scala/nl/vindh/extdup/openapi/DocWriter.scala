package nl.vindh.extdup.openapi

import java.io.{File, PrintWriter}
import sttp.apispec.openapi.circe.yaml._

object DocWriter extends App with Docs {


  val filePath = new File("target/openapi/openapi.yaml")
  filePath.getParentFile.mkdirs()
  val printWriter = new PrintWriter(filePath)
  printWriter.print(docs.toYaml)
  printWriter.close()
}
