import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "tapir-demo",
    libraryDependencies ++= Seq(
      Cats.core,
      Circe.core,
      Circe.generic,
      Tapir.core,
      Tapir.circe,
      Tapir.swagger,
      Tapir.akka,
      Other.log4Cats,
      Other.logback
    )
  )
