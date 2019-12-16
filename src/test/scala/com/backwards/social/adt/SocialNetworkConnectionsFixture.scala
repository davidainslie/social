package com.backwards.social.adt

import io.circe.Json
import io.circe.literal._

trait SocialNetworkConnectionsFixture {
  val facebookSocialNetworkConnections: Json = json"""{
    "sn": "facebook",
    "people": [{ "name": "John" }, { "name": "Harry" }, { "name": "Peter" }, { "name": "George" }, { "name": "Anna" }],
    "relationships": [{
      "type": "HasConnection", "startNode": "John", "endNode": "Peter"
    }, {
      "type": "HasConnection", "startNode": "John", "endNode": "George"
    }, {
      "type": "HasConnection", "startNode": "Peter", "endNode": "George"
    }, {
      "type": "HasConnection", "startNode": "Peter", "endNode": "Anna"
    }]
  }"""
}