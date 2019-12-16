package com.backwards.social.adt

import org.scalatest.matchers.must.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.social.adt.SocialNetworkConnections._
import com.backwards.social.adt.SocialNetworkConnectionsCodec._

class SocialNetworkConnectionsSpec extends AnyWordSpec with Matchers with TableDrivenPropertyChecks with SocialNetworkConnectionsFixture {
  spec =>

  "Social network connections" should {
    val Right(facebookSocialNetworkConnections) = spec.facebookSocialNetworkConnections.as[SocialNetworkConnections]

    "highlight who has no relationships" in {
      noRelationships(facebookSocialNetworkConnections) mustEqual List(User("Harry"))
    }

    "give degree counts for a given user" in {
      val firstDegree = SocialNetworkConnections.firstDegreeCount(_: User)(facebookSocialNetworkConnections)
      val secondDegree = SocialNetworkConnections.secondDegreeCount(_: User)(facebookSocialNetworkConnections)

      val users = Table(
        ("User",        "First Degree Count",   "Second Degree Count"),
        (User("John"),  FirstDegreeCount(2),    SecondDegreeCount(1)),
        (User("Peter"), FirstDegreeCount(3),    SecondDegreeCount(0)),
        (User("Anna"),  FirstDegreeCount(1),    SecondDegreeCount(2))
      )

      forAll(users) { (user: User, firstDegreeCount: FirstDegreeCount, secondDegreeCount: SecondDegreeCount) =>
        firstDegree(user) mustEqual firstDegreeCount
        secondDegree(user) mustEqual secondDegreeCount
      }
    }
  }
}