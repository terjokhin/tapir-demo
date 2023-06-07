package org.daron.tapir.demo

import errors.ServiceError
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

object Endpoints {

  private val errors = oneOf[ServiceError](
    oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[ServiceError.NotFound].description("Not found!"))),
    oneOfDefaultVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[ServiceError.Other].description("Oops")))
  )

  val health: PublicEndpoint[Unit, ServiceError, Unit, Any] = endpoint.get.in("api" / "health").errorOut(errors).tag("Utility")


}
