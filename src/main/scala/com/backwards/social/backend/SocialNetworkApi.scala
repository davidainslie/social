package com.backwards.social.backend

import cats.MonadError
import cats.implicits._
import sttp.client._
import sttp.client.circe._
import com.backwards.social.adt.SocialNetworkConnectionsCodec._
import com.backwards.social.adt.{Facebook, SocialNetwork, SocialNetworkConnections, Twitter}

trait SocialNetworkApi[F[_], N <: SocialNetwork] {
  def get: F[SocialNetworkConnections]
}

object SocialNetworkApi {
  implicit def facebookApi[F[_], S](implicit Backend: SttpBackend[F, S, NothingT], M: MonadError[F, Throwable]): SocialNetworkApi[F, Facebook.type] =
    new SocialNetworkApi[F, Facebook.type] {
      def get: F[SocialNetworkConnections] =
        basicRequest.get(uri"https://my-third-party/facebook").response(asJson[SocialNetworkConnections]).send()
          .flatMap(_.body.fold(M.raiseError, M.pure))
    }

  implicit def twitterApi[F[_], S](implicit Backend: SttpBackend[F, S, NothingT], M: MonadError[F, Throwable]): SocialNetworkApi[F, Twitter.type] =
    new SocialNetworkApi[F, Twitter.type] {
      def get: F[SocialNetworkConnections] =
        basicRequest.get(uri"https://my-third-party/twitter").response(asJson[SocialNetworkConnections]).send()
          .flatMap(_.body.fold(M.raiseError, M.pure))
    }
}