package com.backwards.social.frontend

import cats.effect.{ContextShift, IO}
import cats.implicits._
import sttp.client.asynchttpclient.cats.AsyncHttpClientCatsBackend
import sttp.client.testing.SttpBackendStub
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.{HttpRoutes, Uri}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.social.adt.{Facebook, SocialNetworkConnections, SocialNetworkConnectionsFixture}

// TODO - WIP: Not really part of the requirements, but decided to make a start.
class SocialNetworkRoutesSpec extends AnyWordSpec with Matchers with Http4sDsl[IO] with Http4sClientDsl[IO] with EntityCodecs with SocialNetworkConnectionsFixture {
  implicit val cs: ContextShift[IO] = IO.contextShift(scala.concurrent.ExecutionContext.global)

  implicit val backend: SttpBackendStub[IO, Nothing] = SttpBackendStub(AsyncHttpClientCatsBackend[IO]().unsafeRunSync)
    .whenRequestMatches(_ => true)
    .thenRespond(SocialNetworkConnections(Facebook, Nil, Nil))

  val routes: HttpRoutes[IO] = SocialNetworkRoutes[IO]

  "Social network routes" should {
    "get no relationship users for Facebook" in {
      val response = for {
        request <- GET(Uri.uri("/facebook/no-relationships"))
        response <- routes.orNotFound.run(request)
      } yield
        response

      response.unsafeRunSync
    }

    "get no relationship users for Twitter" in {
      val response = for {
        request <- GET(Uri.uri("/twitter/no-relationships"))
        response <- routes.orNotFound.run(request)
      } yield
        response

      response.unsafeRunSync
    }
  }
}