import cats.effect.IO
import cats.effect.unsafe.implicits.global
import errors.ServiceError
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.Future

object Logic {

  private val logger = Slf4jLogger.getLogger[IO]

  def health: Future[Either[ServiceError, Unit]] = logger.info("Checking Health!").as(Right(())).unsafeToFuture()

}
