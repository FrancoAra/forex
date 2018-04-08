package forex.services.mediator

import cats.implicits._
import cats.effect.Sync
import forex.domain.Rate

import scala.collection.mutable

class RateStoreMem[F[_]](implicit F: Sync[F]) extends RateStore[F] {

  val store: mutable.HashMap[String, RatePoint] =
    mutable.HashMap.empty

  /** Save or overwrite */
  override def update(point: RatePoint): F[RatePoint] =
    F.delay(store.update(point.id, point)) *> point.pure[F]

  override def save(rate: Rate): F[RatePoint] = {
    val newRate = RatePoint(0, rate)
    F.delay(store += (newRate.id -> newRate)) *> newRate.pure[F]
  }

  override def read(id: Rate.Pair): F[Option[RatePoint]] =
    F.delay(store.get(id.id).map { data =>
      if (data.rate.pair.from == id.from) data
      else data.swap
    })

  override def delete(id: Rate.Pair): F[Option[RatePoint]] =
    F.delay(store.remove(id.id))

  override def withAll(f: RatePoint => F[Unit]): F[Unit] =
    F.suspend(store.values.toList.traverse(f) *> F.unit)
}
