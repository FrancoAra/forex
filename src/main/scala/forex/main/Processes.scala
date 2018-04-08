package forex.main

import cats.Eval
import cats.effect.IO
import forex.config._
import forex.services.mediator.RateCache.RateCacheConfig
import forex.services.mediator.RateProviderOneForge.OneForgeConfig
import forex.services.mediator._
import org.http4s.client.blaze.Http1Client
import org.zalando.grafter.macros._
import org.zalando.grafter._

@readerOf[ApplicationConfig]
case class Processes(
    oneForgeConfig: OneForgeConfig,
    rateCacheConfig: RateCacheConfig,
    executors: Executors
  ) extends Start with Stop {

  var cache: RateCache[IO] = _

  var cacheTurnOff: IO[Unit] = _

  val buildCache: IO[RateCache[IO]] = for {
    client <- Http1Client[IO]()
    store = new RateStoreMem[IO]
    provider = new RateProviderOneForge[IO](client, oneForgeConfig)
    cache = new RateCache[IO](rateCacheConfig, store, provider)
  } yield cache

  override def start: Eval[StartResult] =
    StartResult.eval("Rate Cache") {
      cache = buildCache.unsafeRunSync()
      cacheTurnOff = cache.turnOn(executors.default).unsafeRunSync()
    }

  override def stop: Eval[StopResult] =
    StopResult.eval("Rate Cache") {
      cacheTurnOff.unsafeRunSync()
    }
}
