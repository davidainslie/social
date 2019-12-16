package com.backwards.social.backend

import scala.concurrent.ExecutionContext
import scala.util.Try
import cats.effect.{ContextShift, IO}
import cats.implicits._
import sttp.client._
import sttp.client.asynchttpclient.cats.AsyncHttpClientCatsBackend
import sttp.client.testing.SttpBackendStub
import sttp.model.{Methods, StatusCodes}
import org.scalatest.TryValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.social.adt
import com.backwards.social.adt.{Facebook, SocialNetworkConnections, Twitter}
import com.backwards.social.backend.SocialNetworkApi._

class SocialNetworkApiSpec extends AnyWordSpec with Matchers with TryValues with Methods with StatusCodes {
  "Facebook API" should {
    val facebookBackend: SttpBackendStub[Try, Nothing]#WhenRequest = SttpBackendStub(TryHttpURLConnectionBackend())
      .whenRequestMatches(r => r.method == GET && r.uri.path.startsWith(List("facebook")))

    val socialNetworkConnections = SocialNetworkConnections(Facebook, Nil, Nil)

    "get all social network connections" in {
      implicit val backend: SttpBackendStub[Try, Nothing] =
        facebookBackend thenRespond Right(socialNetworkConnections)

      facebookApi.get.success.value mustEqual socialNetworkConnections
    }

    "get all social network connections asynchronously" in {
      implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.Implicits.global)

      implicit val backend: SttpBackendStub[IO, Nothing] = SttpBackendStub(AsyncHttpClientCatsBackend[IO]().unsafeRunSync)
        .whenRequestMatches(r => r.method == GET && r.uri.path.startsWith(List("facebook")))
        .thenRespond(Right(socialNetworkConnections))

      facebookApi[IO, Nothing].get.unsafeRunSync mustEqual socialNetworkConnections
    }

    "fail to get all social network connections" in {
      val exception = new Exception("whoops")

      implicit val backend: SttpBackendStub[Try, Nothing] =
        facebookBackend thenRespond Left(exception)

      facebookApi.get.failure.exception mustEqual exception
    }
  }

  "Twitter API" should {
    val twitterBackend: SttpBackendStub[Try, Nothing]#WhenRequest = SttpBackendStub(TryHttpURLConnectionBackend())
      .whenRequestMatches(r => r.method == GET && r.uri.path.startsWith(List("twitter")))

    val socialNetworkConnections = adt.SocialNetworkConnections(Twitter, Nil, Nil)

    "get all social network connections" in {
      implicit val backend: SttpBackendStub[Try, Nothing] =
        twitterBackend thenRespond Right(socialNetworkConnections)

      twitterApi.get.success.value mustEqual socialNetworkConnections
    }

    "get all social network connections asynchronously (though we cheat to avoid waiting)" in {
      implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.Implicits.global)

      implicit val backend: SttpBackendStub[IO, Nothing] = SttpBackendStub(AsyncHttpClientCatsBackend[IO]().unsafeRunSync)
        .whenRequestMatches(r => r.method == GET && r.uri.path.startsWith(List("twitter")))
        .thenRespond(Right(socialNetworkConnections))

      twitterApi[IO, Nothing].get.unsafeRunSync mustEqual socialNetworkConnections
    }

    "fail to get all social network connections" in {
      val exception = new Exception("whoops")

      implicit val backend: SttpBackendStub[Try, Nothing] =
        twitterBackend thenRespond Left(exception)

      twitterApi.get.failure.exception mustEqual exception
    }
  }
}