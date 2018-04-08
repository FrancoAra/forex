package forex.interfaces.api.utils.marshalling

import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import akka.http.scaladsl.model.HttpResponse
import cats.effect.IO

import scala.concurrent.Future

trait CatsIOSupport {

  implicit def ioMarshaller[T](implicit m: ToResponseMarshaller[Future[T]]): ToResponseMarshaller[IO[T]] =
    Marshaller[IO[T], HttpResponse] { implicit ec ⇒ io ⇒
      m(io.unsafeToFuture())
    }

}

object CatsIOSupport extends CatsIOSupport
