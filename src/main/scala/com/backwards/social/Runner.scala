package com.backwards.social

import cats.effect._
import cats.implicits._
import sttp.client.asynchttpclient.cats.AsyncHttpClientCatsBackend
import sttp.client.{NothingT, SttpBackend}
import org.http4s.HttpRoutes
import com.backwards.social.frontend.{HealthRoutes, SocialNetworkRoutes}

object Runner extends RunnerApp {
  implicit val backend: SttpBackend[IO, Nothing, NothingT] = AsyncHttpClientCatsBackend[IO]().unsafeRunSync()

  val routes: HttpRoutes[IO] =
    HealthRoutes[IO] <+> SocialNetworkRoutes[IO]
}