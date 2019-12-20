package com.backwards.social

import cats.effect._
import cats.implicits._
import sttp.client.Response
import sttp.client.asynchttpclient.cats.AsyncHttpClientCatsBackend
import sttp.client.testing.SttpBackendStub
import sttp.model.Methods
import org.http4s.HttpRoutes
import com.backwards.social.adt.SocialNetworkConnectionsCodec._
import com.backwards.social.adt.{SocialNetworkConnections, SocialNetworkConnectionsFixture, Twitter}
import com.backwards.social.algebra.NetworkingInterpreter
import com.backwards.social.frontend.SocialNetworkRoutes

object RunnerSpec extends RunnerApp with Methods with SocialNetworkConnectionsFixture {
  implicit val backend: SttpBackendStub[IO, Nothing] = SttpBackendStub(AsyncHttpClientCatsBackend[IO]().unsafeRunSync)
    .whenRequestMatchesPartial({
      case r if r.method == GET && r.uri.toString.endsWith("facebook") =>
        Response.ok(facebookSocialNetworkConnections.as[SocialNetworkConnections])

      case r if r.uri.toString.endsWith("twitter") =>
        Response.ok(SocialNetworkConnections(Twitter, Nil, Nil).asRight)
    })

  val routes: HttpRoutes[IO] =
    SocialNetworkRoutes[IO](NetworkingInterpreter[IO])
}