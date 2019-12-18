package com.backwards.social.frontend

import cats.MonadError
import cats.effect.Effect
import sttp.client.{NothingT, SttpBackend}
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Response}
import com.backwards.social.adt.{Facebook, Twitter, User}
import com.backwards.social.algebra.Networking
import com.backwards.social.backend.SocialNetworkApi._
import com.backwards.social.frontend.EntityCodecs._

class SocialNetworkRoutes[F[_]: Effect](networking: Networking[F])(implicit Backend: SttpBackend[F, Nothing, NothingT], M: MonadError[F, Throwable]) extends Http4sDsl[F] {
  import M._

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "relationships" / "facebook" =>
      apply(networking.noRelationships(Facebook))

    case GET -> Root / "relationships" / "twitter" =>
      apply(networking.noRelationships(Twitter))

    case GET -> Root / "relationships" / userName =>
      // TODO - Currently only accessing Facebook. Either access Twitter as well and combine or allow greater flexibility in this endpoint
      flatMap(attempt(networking.relationshipCount(Facebook, User(userName)))) {
        case Right(degreeCounts) => Ok(degreeCounts)
        case Left(t) => BadRequest(t.getMessage)
      }
  }

  def apply(users: F[List[User]]): F[Response[F]] = flatMap(attempt(users)) {
    case Right(users) => Ok(users)
    case Left(t) => BadRequest(t.getMessage)
  }
}

object SocialNetworkRoutes {
  def apply[F[_]: Effect](networking: Networking[F])(implicit Backend: SttpBackend[F, Nothing, NothingT], M: MonadError[F, Throwable]): HttpRoutes[F] =
    new SocialNetworkRoutes[F](networking).routes
}