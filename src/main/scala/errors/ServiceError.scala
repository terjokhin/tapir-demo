package errors

import io.circe.Codec
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

final case class ServiceError(code: Int, message: String)

object ServiceError {

  implicit val codec: Codec[ServiceError] = io.circe.generic.semiauto.deriveCodec[ServiceError]

  val apiErrors = oneOf[ServiceError](
    oneOfVariantValueMatcher(StatusCode.NotFound, jsonBody[ServiceError]) { case ServiceError(code, _) => code == StatusCode.NotFound.code },
    oneOfVariantValueMatcher(StatusCode.Conflict, jsonBody[ServiceError]) { case ServiceError(code, _) => code == StatusCode.Conflict.code },
    oneOfVariantValueMatcher(StatusCode.Unauthorized, jsonBody[ServiceError]) { case ServiceError(code, _) => code == StatusCode.Unauthorized.code },
    oneOfDefaultVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[ServiceError]))
  )
}
