package domain

import cats.effect.kernel.Resource
import cats.effect.{IO, Ref}

class UserService(data: Ref[IO, Map[String, User]]) {
  def save(user: User): IO[Unit]         = data.update(_.updated(user.id, user))
  def find(id: String): IO[Option[User]] = data.get.map(_.get(id))
  def all: IO[List[User]]                = data.get.map(_.values.toList.sortBy(_.id))
}

object UserService {

  def make: Resource[IO, UserService] =
    Resource.eval(Ref.of[IO, Map[String, User]](Map.empty)).map(new UserService(_))
}
