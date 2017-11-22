import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait ItemJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val dataFormatI = jsonFormat5(ItemToReceive)
  implicit val dataFormatI2 = jsonFormat6(ItemForSelect)
}

//trait ItemForSelectJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
//  implicit val dataFormatI = jsonFormat6(ItemForSelect)
//}