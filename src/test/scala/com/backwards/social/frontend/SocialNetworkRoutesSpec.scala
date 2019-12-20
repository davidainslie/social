package com.backwards.social.frontend

import cats.effect.{ContextShift, IO}
import cats.implicits._
import io.circe.Json
import io.circe.literal._
import shapeless.tag
import sttp.client.asynchttpclient.cats.AsyncHttpClientCatsBackend
import sttp.client.testing.SttpBackendStub
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.{HttpRoutes, Uri, _}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.social.adt._
import com.backwards.social.algebra.{Networking, NetworkingInterpreter}

class SocialNetworkRoutesSpec extends AnyWordSpec with Matchers with Http4sDsl[IO] with Http4sClientDsl[IO] with EntityCodecs with SocialNetworkConnectionsFixture {
  implicit val cs: ContextShift[IO] = IO.contextShift(scala.concurrent.ExecutionContext.global)

  val networking: Networking[IO] = NetworkingInterpreter[IO]

  val socialNetworkConnections: SocialNetwork => SocialNetworkConnections =
    SocialNetworkConnections(_, List(User("bob"), User("nomates")), List(HasConnection(tag[StartNode](User("bob")), tag[EndNode](User("su")))))

  "Facebook social network routes" should {
    implicit val backend: SttpBackendStub[IO, Nothing] = SttpBackendStub(AsyncHttpClientCatsBackend[IO]().unsafeRunSync)
      .whenRequestMatches(_ => true)
      .thenRespond(Right(socialNetworkConnections(Facebook)))

    val routes: HttpRoutes[IO] = SocialNetworkRoutes[IO](networking)

    "get users which have no relationships" in {
      val response: Response[IO] =
        GET(Uri.uri("/relationships/facebook")).flatMap(routes.orNotFound.run).unsafeRunSync

      response.status mustEqual Status.Ok
      response.as[Json].unsafeRunSync.as[List[String]] mustEqual List("nomates").asRight
    }

    // TODO - Missing test for API where user name is provided
  }

  "Twitter social network routes" should {
    implicit val backend: SttpBackendStub[IO, Nothing] = SttpBackendStub(AsyncHttpClientCatsBackend[IO]().unsafeRunSync)
      .whenRequestMatches(_ => true)
      .thenRespond(Right(socialNetworkConnections(Twitter)))

    val routes: HttpRoutes[IO] = SocialNetworkRoutes[IO](networking)

    "get users which have no relationships" in {
      val response: Response[IO] =
        GET(Uri.uri("/relationships/twitter")).flatMap(routes.orNotFound.run).unsafeRunSync

      response.status mustEqual Status.Ok
      response.as[Json].unsafeRunSync.as[List[String]] mustEqual List("nomates").asRight
    }

    // TODO - Missing test for API where user name is provided
  }
}