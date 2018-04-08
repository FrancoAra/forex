package forex.services.mediator

import forex.domain.Rate

/** Rate providers algebra */
trait RateProvider[F[_]] {

  def get(pair: Rate.Pair): F[Rate]
}
