package forex.services.mediator

import forex.domain.Rate

/** Rate store algebra, used in the mediator cache to CRUD rate points. */
trait RateStore[F[_]] {

  /** Save or overwrite */
  def update(point: RatePoint): F[RatePoint]

  def save(rate: Rate): F[RatePoint]

  def read(id: Rate.Pair): F[Option[RatePoint]]

  def delete(id: Rate.Pair): F[Option[RatePoint]]

  def withAll(f: RatePoint => F[Unit]): F[Unit]
}
