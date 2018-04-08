package forex.services.mediator

import cats.effect.Sync
import cats.implicits._
import forex.domain.{Price, Rate, Timestamp}
import forex.services.mediator.RateProviderOneForge.{OneForgeConfig, OneForgeResponse}
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.{EntityDecoder, Uri}
import org.http4s.client.Client
import org.http4s.circe._

class RateProviderOneForge[F[_]](client: Client[F], config: OneForgeConfig)(implicit F: Sync[F]) extends RateProvider[F] {

  def uri(pair: Rate.Pair): Uri =
    Uri.unsafeFromString(s"https://forex.1forge.com/1.0.3/quotes?pairs=${pair.show}&api_key=${config.key}")

  override def get(pair: Rate.Pair): F[Rate] =
    client.expect[Array[OneForgeResponse]](uri(pair))(jsonOf[F, Array[OneForgeResponse]]).map(_.head.toRate(pair))
}

object RateProviderOneForge {

  case class OneForgeConfig(key: String)

  case class OneForgeResponse(price: BigDecimal, timestamp: Long) {

    def toRate(pair: Rate.Pair): Rate = Rate(pair, Price(price), Timestamp(timestamp))
  }

  object OneForgeResponse {

    implicit val decoder: Decoder[OneForgeResponse] = deriveDecoder
  }
}
