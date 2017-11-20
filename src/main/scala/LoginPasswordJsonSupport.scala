import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait LoginPasswordJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val dataFormatLP = jsonFormat2(LoginPassword)
}
