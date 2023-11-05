import Dependencies._

name := "extreme-deduplication"

version := "0.1"

scalaVersion := "2.13.12"

lazy val writer = TaskKey[Unit]("writer")

lazy val service = (project in file("service"))
  .dependsOn(domain, api)

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
    writer := (Compile / runMain).toTask(" nl.vindh.extdup.openapi.DocWriter").value
  )

lazy val domain = (project in file("domain"))
