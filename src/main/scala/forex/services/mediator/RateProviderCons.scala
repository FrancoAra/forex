package forex.services.mediator

import cats.implicits._
import cats.effect.Sync
import forex.domain.{Price, Rate, Timestamp}

/** Rate provider interpreter which returns a constant price, and counts the amount of requests. */
final class RateProviderCons[F[_]](price: BigDecimal)(implicit F: Sync[F]) extends RateProvider[F] {

  private var count: Int = 0

  private val increment: F[Unit] = F.delay { count = count + 1 }

  val getCount: F[Int] = F.delay(count)

  override def get(pair: Rate.Pair): F[Rate] =
    increment *> F.pure(Rate(pair, Price(price), Timestamp.now))
}
