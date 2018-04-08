package forex

import cats.effect.IO
import akka.http.scaladsl.testkit.ScalatestRouteTest
import cats.implicits._
import forex.domain.{Currency, Rate}
import forex.services.mediator._
import forex.services.mediator.RateCache.RateCacheConfig
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class RateCacheSpec extends WordSpec with Matchers with ScalatestRouteTest with BeforeAndAfterAll {

  val config: RateCacheConfig = RateCacheConfig(
    tick = 1,
    mortality = 3,
    poolSize = 2
  )

  val price: BigDecimal = BigDecimal(10)

  val store: RateStore[IO] = new RateStoreMem[IO]

  val provider: RateProviderCons[IO] = new RateProviderCons[IO](price)

  val cache: RateCache[IO] = new RateCache[IO](config, store, provider)

  val testPair: Rate.Pair = Rate.Pair(Currency.EUR, Currency.JPY)

  var turnOff: IO[Unit] = _

  override def beforeAll(): Unit = turnOff = cache.turnOn.unsafeRunSync()

  override def afterAll(): Unit = turnOff.unsafeRunSync()

  "RateCache" should {

    "return a simple rate" in {
      val assertion =
        for {
          result <- cache.getRate(testPair)
          providerRequestsCount <- provider.getCount
        } yield assert(result.pair == testPair && result.price.value == price && providerRequestsCount == 1)
      assertion.unsafeRunSync()
    }

    "cache the rates" in {
      val assertion =
        for {
          _ <- IO(Thread.sleep(1000))
          result <- cache.getRate(testPair.swap)
          providerRequestsCount <- provider.getCount
        } yield assert(result.pair == testPair.swap && result.price.value == BigDecimal(0.1) && providerRequestsCount == 1)
      assertion.unsafeRunSync()
    }

    "renew after mortality threshold" in {
      val assertion =
        for {
          _ <- IO(Thread.sleep(2000))
          result <- cache.getRate(testPair)
          providerRequestsCount <- provider.getCount
        } yield assert(result.pair == testPair && result.price.value == price && providerRequestsCount == 2)
      assertion.unsafeRunSync()
    }
  }
}
