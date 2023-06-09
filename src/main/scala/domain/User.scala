package domain

import io.circe.Codec

final case class User(id: String, firstName: String, lastName: String)

object User {
  implicit val codec: Codec[User] = io.circe.generic.semiauto.deriveCodec
}
