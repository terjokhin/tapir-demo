import cats.effect.kernel.Resource
import cats.effect.{ExitCode, IO, IOApp}
import org.typelevel.log4cats.slf4j.Slf4jLogger

object App extends IOApp {

  private val logger = Slf4jLogger.getLogger[IO]

  private def resources: Resource[IO, Unit] = for {
    _        <- Resource.eval(logger.info("Starting Server"))
    services <- Services.make
    routes   <- Routes.make(services)
    _        <- Server.make(routes)
    _        <- Resource.eval(logger.info("Server has started"))
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = resources.use(_ => IO.never)
}
