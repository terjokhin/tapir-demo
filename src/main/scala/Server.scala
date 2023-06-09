import cats.effect.IO
import cats.effect.kernel.Resource
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Server

object Server {

  def make(routes: HttpRoutes[IO]): Resource[IO, Server] = {
    val router = routes.orNotFound
    BlazeServerBuilder[IO].bindLocal(8888).withHttpApp(router).resource
  }
}
