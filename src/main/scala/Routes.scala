import akka.http.scaladsl.server.Route
import org.daron.tapir.demo.Endpoints
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Routes {

  private val swaggerEndpoints = SwaggerInterpreter().fromEndpoints[Future](List(Endpoints.health), "Some Service", "1.0.1")

  val akkaRoutes: Route = AkkaHttpServerInterpreter().toRoute(
    List(
      Endpoints.health.serverLogic(_ => Logic.health)
    ) ++ swaggerEndpoints
  )
}
