import sbt.*

object Dependencies {

  object Cats {
    private val version = "3.4.8"

    val core = "org.typelevel" %% "cats-effect" % version
  }

  object Circe {
    private val version = "0.14.3"
    private val groupId = "io.circe"

    private def lib(artifactId: String) = groupId %% artifactId % version

    val core    = lib("circe-core")
    val generic = lib("circe-generic")
  }

  object Tapir {
    private val version = "1.5.1"
    private val groupId = "com.softwaremill.sttp.tapir"

    private def lib(artifactId: String) = groupId %% artifactId % version

    val core    = lib("tapir-core")
    val circe   = lib("tapir-json-circe")
    val swagger = lib("tapir-swagger-ui-bundle")
    val akka    = lib("tapir-akka-http-server")
  }

  object Other {
    val logback  = "ch.qos.logback" % "logback-classic" % "1.4.7"
    val log4Cats = "org.typelevel" %% "log4cats-slf4j"  % "2.5.0"
  }

}
