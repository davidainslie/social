package com.backwards.social.adt

import io.circe.Json
import io.circe.literal._

trait SocialNetworkConnectionsFixture {
  object NoFacebookRelationships {
    val harry = User("Harry")
  }

  object FacebookRelationshipCount {
    val (john, johnFirstDegreeCount, johnSecondDegreeCount) = (User("John"), FirstDegreeCount(2), SecondDegreeCount(1))
    val (peter, peterFirstDegreeCount, peterSecondDegreeCount) = (User("Peter"), FirstDegreeCount(3), SecondDegreeCount(0))
    val (anna, annaFirstDegreeCount, annaSecondDegreeCount) = (User("Anna"), FirstDegreeCount(1), SecondDegreeCount(2))
  }

  import NoFacebookRelationships._
  import FacebookRelationshipCount._

  val facebookSocialNetworkConnections: Json = json"""{
    "sn": "facebook",
    "people": [{ "name": ${john.name} }, { "name": ${harry.name} }, { "name": ${peter.name} }, { "name": "George" }, { "name": "Anna" }],
    "relationships": [{
      "type": "HasConnection", "startNode": ${john.name}, "endNode": ${peter.name}
    }, {
      "type": "HasConnection", "startNode": ${john.name}, "endNode": "George"
    }, {
      "type": "HasConnection", "startNode": ${peter.name}, "endNode": "George"
    }, {
      "type": "HasConnection", "startNode": ${peter.name}, "endNode": "Anna"
    }]
  }"""
}