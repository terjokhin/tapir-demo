import cats.effect.IO
import domain.{AuthService, SecretKey, User, UserService}
import errors.ServiceError
import org.daron.tapir.demo.Endpoints.UsersRequest
import org.typelevel.log4cats.slf4j.Slf4jLogger
import sttp.model.StatusCode

class Logic(userService: UserService, authService: AuthService) {

  private val logger = Slf4jLogger.getLogger[IO]

  def health: IO[Either[ServiceError, Unit]] = logger.info("Checking Health!").resultOrError

  def auth(input: String): IO[Either[ServiceError, SecretKey]] = authService.verify(input)

  def saveUser(secretKey: SecretKey)(user: User): IO[Either[ServiceError, Unit]] = (for {
    _      <- logger.info(s"Got save request. SecretKey: $secretKey")
    _      <- logger.info(s"Saving $user")
    found  <- userService.find(user.id)
    result <- found match {
                case Some(_) => IO.pure(Left(ServiceError(StatusCode.Conflict.code, s"User with ${user.id} already exists")))
                case None    => userService.save(user).as(Right(()))
              }
  } yield result).handleInternal

  def findUser(id: String): IO[Either[ServiceError, User]] = userService
    .find(id)
    .map {
      case None       => Left(ServiceError(StatusCode.NotFound.code, s"User with id $id not found."))
      case Some(user) => Right(user)
    }
    .handleInternal

  def listUsers(req: UsersRequest): IO[Either[ServiceError, List[User]]] = (for {
    _      <- logger.info(s"Got user list request $req")
    result <- userService.all.map(_.drop(req.offset).take(req.limit))
  } yield result).resultOrError

  implicit class IOOps[A](io: IO[Either[ServiceError, A]]) {
    def handleInternal: IO[Either[ServiceError, A]] = handleThrowable(io)
  }

  implicit class IOOps1[A](io: IO[A]) {
    def resultOrError: IO[Either[ServiceError, A]] = io.map(Right(_)).recover { case err: Throwable =>
      Left(ServiceError(StatusCode.InternalServerError.code, err.getMessage))
    }
  }

  private def handleThrowable[A](io: IO[Either[ServiceError, A]]): IO[Either[ServiceError, A]] =
    io.recover { case err: Throwable => Left(ServiceError(StatusCode.InternalServerError.code, err.getMessage)) }

}
