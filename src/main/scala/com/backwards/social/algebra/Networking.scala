package com.backwards.social.algebra

import com.backwards.social.adt.{SocialNetwork, User}
import com.backwards.social.backend.SocialNetworkApi

trait Networking[F[_]] {
  def noRelationships(sn: SocialNetwork)(implicit Api: SocialNetworkApi[F, sn.type]): F[List[User]]
}