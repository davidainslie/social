package com.backwards.social.frontend

import cats.effect.Effect
import cats.implicits._
import sttp.client.{NothingT, SttpBackend}
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityEncoder, HttpRoutes, Response}
import com.backwards.social.adt.DegreeCountMonoid._
import com.backwards.social.adt.{DegreeCounts, Facebook, Twitter, User}
import com.backwards.social.algebra.Networking
import com.backwards.social.backend.SocialNetworkApi._
import com.backwards.social.frontend.EntityCodecs._

class SocialNetworkRoutes[F[_]: Effect](networking: Networking[F])(implicit Backend: SttpBackend[F, Nothing, NothingT]) extends Http4sDsl[F] {
  val effect: Effect[F] = Effect[F]
  import effect._

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "relationships" / "facebook" / userName =>
      apply(networking.relationshipCount(Facebook, User(userName)))

    case GET -> Root / "relationships" / "twitter" / userName =>
      apply(networking.relationshipCount(Twitter, User(userName)))

    case GET -> Root / "relationships" / "facebook" =>
      apply(networking.noRelationships(Facebook))

    case GET -> Root / "relationships" / "twitter" =>
      apply(networking.noRelationships(Twitter))

    case GET -> Root / "relationships" / userName =>
      val facebookDegreeCounts: F[DegreeCounts] = networking.relationshipCount(Facebook, User(userName))
      val twitterDegreeCounts: F[DegreeCounts] = networking.relationshipCount(Twitter, User(userName))

      apply((facebookDegreeCounts, twitterDegreeCounts).mapN(_ |+| _))
  }

  // TODO - An "injected" MonadError handler will eventually be provided to map business errors to HTTP protocol leaving the folloinwg redundant
  def apply[A](a: F[A])(implicit E: EntityEncoder[F, A]): F[Response[F]] = flatMap(attempt(a)) {
    case Right(a) => Ok(a)
    case Left(t) => BadRequest(t.getMessage)
  }
}

object SocialNetworkRoutes {
  def apply[F[_]: Effect](networking: Networking[F])(implicit Backend: SttpBackend[F, Nothing, NothingT]): HttpRoutes[F] =
    new SocialNetworkRoutes[F](networking).routes
}