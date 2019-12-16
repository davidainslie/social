package com.backwards.social.frontend

import cats.effect.Effect
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class HealthRoutes[F[_]: Effect] extends Http4sDsl[F] {
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "healthz" =>
      Ok()
  }
}

object HealthRoutes {
  def apply[F[_]: Effect]: HttpRoutes[F] =
    new HealthRoutes[F].routes
}