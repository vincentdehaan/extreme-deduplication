import Dependencies._

import scala.io.Source

name := "extreme-deduplication"

version := "0.1"

ThisBuild / scalaVersion := "2.13.12"

lazy val writer = TaskKey[Unit]("writer")

lazy val service = (project in file("service"))
  .dependsOn(domain, api, dbinfo)
  .settings(
    libraryDependencies ++= Seq(
      "org.scanamo" %% "scanamo" % scanamoVersion,
      "org.scanamo" %% "scanamo-testkit" % scanamoVersion
    )
  )

lazy val api = (project in file("api"))
  .dependsOn(domain)
  .settings(
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % tapirVersion
    )
  )

lazy val openapi = (project in file("openapi"))
  .dependsOn(api)
  .settings(
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,
      "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % apiSpecVersion
    ),
    writer := (Compile / runMain).toTask(s" nl.vindh.extdup.openapi.DocWriter").value
  )

lazy val domain = (project in file("domain"))
  .enablePlugins(ScalaTsiPlugin)
  .settings(
    typescriptExports :=
      Source.fromInputStream(
        java.lang.Runtime.getRuntime.exec("find domain/src/main/scala/ -type f -exec cat {} +").getInputStream
      ).getLines().flatMap(l => """case class ([a-zA-Z]+)""".r.findFirstMatchIn(l).map(_.subgroups(0))).toSeq,
    typescriptOutputFile := baseDirectory.value / "target/index.d.ts",
    typescriptGenerationImports := Seq("nl.vindh.extdup.domain._")
  )

lazy val dbinfo = (project in file("dbinfo"))
  .dependsOn(domain)
  .settings(
    libraryDependencies += "com.chuusai" %% "shapeless" % shapelessVersion
  )

lazy val cloudformation = (project in file("cloudformation"))
  .dependsOn(dbinfo)
  .settings(
    libraryDependencies += "io.jobial" %% "cloud-formation-template-generator" % "3.10.4"
  )