import cats.effect.{IO, Resource}
import domain.{AuthService, UserService}

final case class Services(userService: UserService, authService: AuthService)

object Services {
  def make: Resource[IO, Services] = UserService.make.map(Services(_, new AuthService))
}
