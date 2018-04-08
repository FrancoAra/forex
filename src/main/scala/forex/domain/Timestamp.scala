package forex.domain

import io.circe._
import io.circe.generic.extras.wrapped._

case class Timestamp(value: Long) extends AnyVal

object Timestamp {
  def now: Timestamp =
    Timestamp(System.currentTimeMillis())

  implicit val encoder: Encoder[Timestamp] =
    deriveUnwrappedEncoder[Timestamp]

  implicit val decoder: Decoder[Timestamp] =
    deriveUnwrappedDecoder[Timestamp]
}
