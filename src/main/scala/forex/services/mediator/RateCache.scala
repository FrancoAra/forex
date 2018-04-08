package forex.services.mediator

import cats.Monad
import cats.effect.{Effect, IO}
import cats.implicits._
import fs2.{Scheduler, Stream}

import scala.concurrent.duration._
import forex.domain.Rate
import forex.services.mediator.RateCache.RateCacheConfig
import fs2.async.mutable.Signal
import monocle.macros.syntax.lens._

import scala.concurrent.ExecutionContext

/** Used with a store to cache responses from a rate provider. */
class RateCache[F[_]](config: RateCacheConfig, store: RateStore[F], provider: RateProvider[F])(implicit F: Effect[F]) {

  def turnOn(implicit ec: ExecutionContext): IO[IO[Unit]] =
    for {
      interrupter <- Signal[IO, Boolean](false)
      _ <- agingStream(interrupter).compile.drain.runAsync(_ => IO.unit)
    } yield interrupter.set(true)

  def getRate(pair: Rate.Pair): F[Rate] =
    for {
      optRate <- store.read(pair)
      ratePoint <- optRate.fold
      { provider.get(pair) >>= store.save }
      { F.pure }
    } yield ratePoint.rate

  private def agingStream(interrupter: Signal[IO, Boolean])(implicit ec: ExecutionContext): Stream[IO, Unit] =
    Scheduler[IO](config.poolSize)
      .flatMap[Unit](_
        .awakeEvery[IO](config.tick.seconds)
        .evalMap[Unit](_ => F.runAsync(ageData)(_ => IO.unit))
        .interruptWhen(interrupter)
      )

  private def ageData: F[Unit] =
    incrementAge *> killOld

  private def incrementAge: F[Unit] =
    store.withAll { data =>
      store.update(data.lens(_.age).modify(_ + config.tick)) *> F.unit
    }

  private def killOld: F[Unit] =
    store.withAll { data =>
      if (data.age < config.mortality) F.unit
      else store.delete(data.rate.pair) *> F.unit
    }
}

object RateCache {

  case class RateCacheConfig(tick: Int, mortality: Int, poolSize: Int = 5)
}
