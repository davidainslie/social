package com.backwards.social.adt

import cats.implicits._
import io.circe.generic.AutoDerivation
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}
import shapeless.tag
import com.backwards.social.adt

object SocialNetworkConnectionsCodec extends SocialNetworkConnectionsCodec

trait SocialNetworkConnectionsCodec extends AutoDerivation {
  implicit val socialNetworkDecoder: Decoder[SocialNetwork] = Decoder.decodeString.emap {
    case "facebook" => Facebook.asRight
    case "twitter" => Twitter.asRight
    case s => s"Unrecognised social network: $s".asLeft
  }

  implicit val socialNetworkEncoder: Encoder[SocialNetwork] = Encoder.instance {
    case Facebook => "facebook".asJson
    case Twitter => "twitter".asJson
  }

  implicit val hasConnectionDecoder: Decoder[HasConnection] =
    Decoder.forProduct2("startNode", "endNode") { (startNode: String, endNode: String) =>
      adt.HasConnection(tag[StartNode](User(startNode)), tag[EndNode](User(endNode)))
    }

  implicit val hasConnectionEncoder: Encoder[HasConnection] =
    (adt: HasConnection) => Json.obj(
      ("type", Json.fromString("HasConnection")),
      ("startNode", Json.fromString(adt.startNode.name)),
      ("endNode", Json.fromString(adt.endNode.name))
    )

  implicit val relationshipDecoder: Decoder[Relationship] = List[Decoder[Relationship]](
    Decoder[HasConnection].widen
  ).reduceLeft(_ or _)

  implicit val relationshipEncoder: Encoder[Relationship] = Encoder.instance {
    case h @ HasConnection(_, _) => h.asJson
  }
}