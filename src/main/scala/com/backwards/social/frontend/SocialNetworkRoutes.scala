package com.backwards.social.frontend

import cats.MonadError
import cats.effect.Effect
import sttp.client.{NothingT, SttpBackend}
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityEncoder, HttpRoutes, Response}
import com.backwards.social.adt.{Facebook, Twitter, User}
import com.backwards.social.algebra.Networking
import com.backwards.social.backend.SocialNetworkApi._
import com.backwards.social.frontend.EntityCodecs._

class SocialNetworkRoutes[F[_]: Effect](networking: Networking[F])(implicit Backend: SttpBackend[F, Nothing, NothingT], M: MonadError[F, Throwable]) extends Http4sDsl[F] {
  import M._

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    // TODO - Join endpoints for Facebook and Twitter for a user e.g.
    // TODO - case GET -> Root / "relationships" / userName

    case GET -> Root / "relationships" / "facebook" / userName =>
      apply(networking.relationshipCount(Facebook, User(userName)))

    case GET -> Root / "relationships" / "twitter" / userName =>
      apply(networking.relationshipCount(Twitter, User(userName)))

    case GET -> Root / "relationships" / "facebook" =>
      apply(networking.noRelationships(Facebook))

    case GET -> Root / "relationships" / "twitter" =>
      apply(networking.noRelationships(Twitter))
  }

  def apply[A](a: F[A])(implicit E: EntityEncoder[F, A]): F[Response[F]] = flatMap(attempt(a)) {
    case Right(a) => Ok(a)
    case Left(t) => BadRequest(t.getMessage)
  }
}

object SocialNetworkRoutes {
  def apply[F[_]: Effect](networking: Networking[F])(implicit Backend: SttpBackend[F, Nothing, NothingT], M: MonadError[F, Throwable]): HttpRoutes[F] =
    new SocialNetworkRoutes[F](networking).routes
}