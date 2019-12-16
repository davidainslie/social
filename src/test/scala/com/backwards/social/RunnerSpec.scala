package com.backwards.social

import cats.effect._
import cats.implicits._
import sttp.client.asynchttpclient.cats.AsyncHttpClientCatsBackend
import sttp.client.testing.SttpBackendStub
import sttp.model.Methods
import org.http4s.HttpRoutes
import com.backwards.social.adt.SocialNetworkConnectionsCodec._
import com.backwards.social.adt.{SocialNetworkConnections, SocialNetworkConnectionsFixture}
import com.backwards.social.frontend.SocialNetworkRoutes

object RunnerSpec extends RunnerApp with Methods with SocialNetworkConnectionsFixture {
  implicit val backend: SttpBackendStub[IO, Nothing] = SttpBackendStub(AsyncHttpClientCatsBackend[IO]().unsafeRunSync)
    .whenRequestMatches(r => r.method == GET && r.uri.toString.endsWith("facebook"))
    .thenRespond(facebookSocialNetworkConnections.as[SocialNetworkConnections])

  val routes: HttpRoutes[IO] = SocialNetworkRoutes[IO]
}