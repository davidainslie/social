package com.backwards.social.frontend

import cats.effect.Effect
import io.circe._
import io.circe.generic.AutoDerivation
import io.circe.generic.extras.decoding.UnwrappedDecoder
import io.circe.generic.extras.encoding.UnwrappedEncoder
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

object EntityCodecs extends EntityCodecs

trait EntityCodecs extends AutoDerivation {
  implicit def valueClassEncoder[A: UnwrappedEncoder]: Encoder[A] = implicitly

  implicit def valueClassDecoder[A: UnwrappedDecoder]: Decoder[A] = implicitly

  implicit def jsonEncoder[F[_]: Effect, A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[F, A]

  implicit def jsonDecoder[F[_]: Effect, A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]
}