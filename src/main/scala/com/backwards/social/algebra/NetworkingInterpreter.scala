package com.backwards.social.algebra

import cats.Monad
import cats.implicits._
import com.backwards.social.adt.SocialNetworkConnections._
import com.backwards.social.adt.{DegreeCounts, SocialNetwork, SocialNetworkConnections, User}
import com.backwards.social.backend.SocialNetworkApi

class NetworkingInterpreter[F[_]: Monad] extends Networking[F] {
  def noRelationships(sn: SocialNetwork)(implicit Api: SocialNetworkApi[F, sn.type]): F[List[User]] =
    Api.get.map(SocialNetworkConnections.noRelationships)

  def relationshipCount(sn: SocialNetwork, user: User)(implicit Api: SocialNetworkApi[F, sn.type]): F[DegreeCounts] = {
    val connections = Api.get

    (connections.map(firstDegreeCount(user)), connections.map(secondDegreeCount(user))).mapN(DegreeCounts)
  }
}