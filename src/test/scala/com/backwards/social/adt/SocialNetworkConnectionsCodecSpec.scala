package com.backwards.social.adt

import io.circe.syntax._
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.backwards.social.adt.SocialNetworkConnectionsCodec._

class SocialNetworkConnectionsCodecSpec extends AnyWordSpec with Matchers with SocialNetworkConnectionsFixture {
  "Social network connections ADT" should {
    "be decoded from JSON and encoded back to ADT" in {
      val Right(adt) = facebookSocialNetworkConnections.as[SocialNetworkConnections]
      adt.sn mustEqual Facebook

      adt.asJson mustEqual facebookSocialNetworkConnections
    }
  }
}