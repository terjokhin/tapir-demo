package org.daron.tapir.demo

import domain.User
import errors.ServiceError
import errors.ServiceError.apiErrors
import org.daron.tapir.demo.Endpoints.UsersRequest
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

class Endpoints {

  val health: PublicEndpoint[Unit, ServiceError, Unit, Any] = endpoint.get.in("api" / "health").errorOut(apiErrors).tag("Utility")

  val saveUser: Endpoint[String, User, ServiceError, Unit, Any] =
    endpoint.post
      .securityIn(auth.apiKey(header[String]("key")))
      .in("api" / "user" / "save")
      .in(jsonBody[User])
      .errorOut(apiErrors)
      .tag("users")

  val findUser: PublicEndpoint[String, ServiceError, User, Any] =
    endpoint.get
      .in("api" / "user" / "find" / path[String]("userId"))
      .out(jsonBody[User])
      .errorOut(apiErrors)
      .tag("users")

  val listUsers: PublicEndpoint[UsersRequest, ServiceError, List[User], Any] =
    endpoint.get
      .in("api" / "user" / "list")
      .in(query[Int]("offset"))
      .in(query[Int]("limit"))
      .in(stringBody)
      .mapInTo[UsersRequest]
      .out(jsonBody[List[User]])
      .errorOut(apiErrors)
      .tag("users")

}

object Endpoints {
  final case class UsersRequest(offset: Int, limit: Int, someOtherInfo: String)
}
