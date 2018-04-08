package forex

import cats.Eval
import com.typesafe.scalalogging._
import forex.config._
import forex.main._
import org.zalando.grafter._

import cats.implicits._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import forex.config.ApplicationConfig
import forex.interfaces.api.rates.Protocol.GetApiResponse
import forex.main.Application
import org.zalando.grafter.Rewriter

class SimpleServiceTests extends WordSpec with Matchers with ScalatestRouteTest with BeforeAndAfterAll with LazyLogging {

  import forex.interfaces.api.utils.ApiMarshallers._

  var app: Option[Application] = None

  var route: Route = _

  override def beforeAll(): Unit = {
    pureconfig.loadConfig[ApplicationConfig]("app") match {
      case Left(errors) ⇒
        logger.error(s"Errors loading the configuration:\n${errors.toList.mkString("- ", "\n- ", "")}")
      case Right(applicationConfig) ⇒
        val application = configure[Application](applicationConfig).configure()

        Rewriter
          .startAll(application)
          .flatMap {
            case results if results.exists(!_.success) ⇒
              logger.error(toStartErrorString(results))
              Rewriter.stopAll(application).map(_ ⇒ ())
            case _ ⇒
              route = application.api.routes.route
              Eval.now { app = Some(application) }
          }
          .value
    }
  }

  override def afterAll(): Unit = {
    app.foreach(Rewriter.stopAll(_))
  }

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
