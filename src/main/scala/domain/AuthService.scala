package domain

import cats.effect.IO
import errors.ServiceError
import sttp.model.StatusCode

class AuthService {

  def verify(input: String): IO[Either[ServiceError, SecretKey]] = {
    if (input == "secret") {
      IO.pure(Right(SecretKey(input)))
    } else {
      IO.pure(Left(ServiceError(StatusCode.Unauthorized.code, "You are not allowed to do that")))
    }
  }

}
