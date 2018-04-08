package forex.domain

import cats.implicits._
import io.circe._
import io.circe.generic.semiauto._

case class Rate(
    pair: Rate.Pair,
    price: Price,
    timestamp: Timestamp
) {

  /** In essence one should be able to get the price of 'y' to 'x' given the price of 'x' to 'y' by this simple formula:
    *   1 / price(x, y)
    *
    * This method computes such equivalent rate. It is used to compute rates from already cached equivalent rates.
    */
  def swap: Rate =
    copy(pair = pair.swap, price = Price(BigDecimal(1) / price.value))
}

object Rate {

  final case class Pair(from: Currency, to: Currency) {

    /** Checks the order of the currency pair names */
    val swapped: Boolean = from < to

    /** Gives the same id to pairs and their swapped versions */
    val id: String =
      if(!swapped) from.show + to.show
      else to.show + from.show

    def swap: Pair = Pair(to, from)
  }

  object Pair {
    implicit val encoder: Encoder[Pair] =
      deriveEncoder[Pair]
  }

  implicit val encoder: Encoder[Rate] =
    deriveEncoder[Rate]
}
