import cats.effect.{IO, Resource}
import org.daron.tapir.demo.Endpoints
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

class Routes(endpoints: Endpoints, logic: Logic) {

  private val swaggerEndpoints =
    SwaggerInterpreter()
      .fromEndpoints[IO](List(endpoints.health, endpoints.saveUser, endpoints.findUser, endpoints.listUsers), "Some Service", "1.0.1")

  val routes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(
    List(
      endpoints.health.serverLogic(_ => logic.health),
      endpoints.saveUser.serverSecurityLogic(logic.auth).serverLogic(logic.saveUser),
      endpoints.findUser.serverLogic(logic.findUser),
      endpoints.listUsers.serverLogic(logic.listUsers)
    ) ++ swaggerEndpoints
  )
}

object Routes {

  def make(services: Services): Resource[IO, HttpRoutes[IO]] = {
    val endpoints = new Endpoints
    val logic     = new Logic(services.userService, services.authService)

    Resource.pure(new Routes(endpoints, logic).routes)
  }
}
