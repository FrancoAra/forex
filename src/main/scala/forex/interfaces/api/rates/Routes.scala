package forex.interfaces.api.rates

import akka.http.scaladsl._
import forex.config._
import forex.domain.Rate
import forex.main._
import forex.interfaces.api.utils._
import org.zalando.grafter.macros._

@readerOf[ApplicationConfig]
case class Routes(
    processes: Processes,
    runners: Runners
) {
  import server.Directives._
  import Directives._
  import Converters._
  import ApiMarshallers._

  import processes._

  lazy val route: server.Route =
    get {
      getApiRequest { req ⇒
        complete {
          cache
            .getRate(Rate.Pair(req.from, req.to))
            .map(toGetApiResponse)
        }
      }
    }

}
