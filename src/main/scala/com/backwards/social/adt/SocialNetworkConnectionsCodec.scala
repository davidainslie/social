package com.backwards.social.adt

import cats.implicits._
import io.circe.Decoder
import io.circe.generic.AutoDerivation
import shapeless.tag
import com.backwards.social.adt

/**
 * So far, not a full Codec as only implemented the necessary Decoders - Initially Encoders are not required
 */
object SocialNetworkConnectionsCodec extends AutoDerivation {
  implicit val socialNetworkDecoder: Decoder[SocialNetwork] = Decoder.decodeString.emap {
    case "facebook" => Facebook.asRight
    case "twitter" => Twitter.asRight
    case s => s"Unrecognised social network: $s".asLeft
  }

  implicit val hasConnectionDecoder: Decoder[HasConnection] =
    Decoder.forProduct2("startNode", "endNode") { (startNode: String, endNode: String) =>
      adt.HasConnection(tag[StartNode](User(startNode)), tag[EndNode](User(endNode)))
    }

  implicit val relationshipDecoder: Decoder[Relationship] = List[Decoder[Relationship]](
    Decoder[HasConnection].widen
  ).reduceLeft(_ or _)
}