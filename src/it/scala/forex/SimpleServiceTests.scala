package forex

import cats.implicits._
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import forex.interfaces.api.rates.Protocol.GetApiResponse
import forex.interfaces.api.rates.Routes
import forex.main.{Processes, Runners}

class SimpleServiceTests extends WordSpec with Matchers with ScalatestRouteTest {

  import forex.interfaces.api.utils.ApiMarshallers._

  val route: Route = Routes(
    Processes(),
    Runners()
  ).route

  "The service" should {

    "return a simple rate" in {
      val from = "EUR"
      val to = "JPY"
      Get(s"/?from=$from&to=$to") ~> route ~> check {
        val response = responseAs[GetApiResponse]
        response.from.show shouldEqual from
        response.to.show shouldEqual to
      }
    }
  }
}
