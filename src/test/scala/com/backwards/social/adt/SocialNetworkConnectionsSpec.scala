package com.backwards.social.adt

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.social.adt.SocialNetworkConnections._
import com.backwards.social.adt.SocialNetworkConnectionsCodec._

class SocialNetworkConnectionsSpec extends AnyWordSpec with Matchers with SocialNetworkConnectionsFixture {
  spec =>

  // TODO - Should have used data tables in the following examples and avoid the extra "objects" in SocialNetworkConnectionsFixture
  "Social network connections" should {
    val Right(facebookSocialNetworkConnections) = spec.facebookSocialNetworkConnections.as[SocialNetworkConnections]

    "highlight who has no relationships" in {
      noRelationships(facebookSocialNetworkConnections) mustEqual List(NoFacebookRelationships.harry)
    }

    "give first degree count for a given user" in {
      val firstDegreeCount = SocialNetworkConnections.firstDegreeCount(_: User)(facebookSocialNetworkConnections)

      firstDegreeCount(FacebookRelationshipCount.john) mustEqual FacebookRelationshipCount.johnFirstDegreeCount
      firstDegreeCount(FacebookRelationshipCount.peter) mustEqual FacebookRelationshipCount.peterFirstDegreeCount
      firstDegreeCount(FacebookRelationshipCount.anna) mustEqual FacebookRelationshipCount.annaFirstDegreeCount
    }

    "give second degree count for a given user" in {
      val secondDegreeCount = SocialNetworkConnections.secondDegreeCount(_: User)(facebookSocialNetworkConnections)

      secondDegreeCount(FacebookRelationshipCount.john) mustEqual FacebookRelationshipCount.johnSecondDegreeCount
      secondDegreeCount(FacebookRelationshipCount.peter) mustEqual FacebookRelationshipCount.peterSecondDegreeCount
      secondDegreeCount(FacebookRelationshipCount.anna) mustEqual FacebookRelationshipCount.annaSecondDegreeCount
    }
  }
}