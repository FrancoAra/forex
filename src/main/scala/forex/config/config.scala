package forex.config

import forex.services.mediator.RateCache.RateCacheConfig
import forex.services.mediator.RateProviderOneForge.OneForgeConfig
import org.zalando.grafter.macros._

import scala.concurrent.duration.FiniteDuration

@readers
case class ApplicationConfig(
  akka: AkkaConfig,
  api: ApiConfig,
  executors: ExecutorsConfig,
  oneForge: OneForgeConfig,
  rateCache: RateCacheConfig
)

case class AkkaConfig(
  name: String,
  exitJvmTimeout: Option[FiniteDuration]
)

case class ApiConfig(
  interface: String,
  port: Int
)

case class ExecutorsConfig(
  default: String
)
