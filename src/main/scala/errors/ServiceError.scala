package errors

import io.circe.{Codec, Encoder}

sealed trait ServiceError

object ServiceError {

  final case class NotFound(id: String, entity: String) extends ServiceError
  final case class Other(message: String)               extends ServiceError

  implicit val codecNotFound: Codec[NotFound] = io.circe.generic.semiauto.deriveCodec
  implicit val codecOther: Codec[Other]       = io.circe.generic.semiauto.deriveCodec

  implicit val encoder: Encoder[ServiceError] = {
    case x: NotFound => codecNotFound(x)
    case x: Other    => codecOther(x)
  }
}
