import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import cats.effect.kernel.Resource
import cats.effect.{ExitCode, IO, IOApp}
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Server extends IOApp {

  private val logger = Slf4jLogger.getLogger[IO]

  def makeRoutes: Resource[IO, Route] = Resource.pure(Routes.akkaRoutes)

  def makeSystem(name: String): Resource[IO, ActorSystem] =
    Resource.make(IO(ActorSystem(name)))(ac => IO.fromFuture(IO(ac.terminate())).void)

  def makeServer(route: Route, actorSystem: ActorSystem): Resource[IO, ServerBinding] = Resource.make(acruire(route, actorSystem))(release)

  private def acruire(route: Route, actorSystem: ActorSystem): IO[ServerBinding] = {
    implicit val ac = actorSystem

    for {
      _      <- logger.info("Starting Server")
      result <- IO.fromFuture(IO(Http().newServerAt("localhost", 8888).bind(route)))
    } yield result
  }

  private def release(b: ServerBinding): IO[Unit] = for {
    _ <- logger.info("Stopping Server")
    _ <- IO.fromFuture(IO(b.unbind()))
  } yield ()

  private def resources: Resource[IO, Unit] = for {
    routes <- makeRoutes
    system <- makeSystem("BestSystem")
    _      <- makeServer(routes, system)
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = resources.use(_ => IO.never)
}
