package com.backwards.social.algebra

import com.backwards.social.adt.{DegreeCounts, SocialNetwork, User}
import com.backwards.social.backend.SocialNetworkApi

trait Networking[F[_]] {
  def noRelationships(sn: SocialNetwork)(implicit Api: SocialNetworkApi[F, sn.type]): F[List[User]]

  def relationshipCount(sn: SocialNetwork, user: User)(implicit Api: SocialNetworkApi[F, sn.type]): F[DegreeCounts]
}