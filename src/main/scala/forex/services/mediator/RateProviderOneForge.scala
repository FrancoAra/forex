package forex.services.mediator

import forex.domain.Rate

class RateProviderOneForge[F[_]]() extends RateProvider[F] {

  override def get(pair: Rate.Pair) = ???
}
