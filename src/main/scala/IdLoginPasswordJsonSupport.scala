import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait IdLoginPasswordJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val dataFormatILP = jsonFormat3(IdLoginPassword)
}

