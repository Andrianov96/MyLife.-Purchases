package mypackage

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import handlers.RequestHandler
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol

object UsefulThings {

  case class ItemToReceive(name: String, price: String, date: String, place: String, itemType: String)  {}
  case class ItemForSelect(name: String, priceLEG: String, price: String, dateLEG:String, date: String, place: String, itemType: String)

  trait ItemJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val dataFormatI = jsonFormat5(ItemToReceive)
  }
  trait ItemForSelectJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val dataFormatI2 = jsonFormat7(ItemForSelect)
  }

  case class IdLoginPassword(id: BigDecimal, login: String, password: String)
  trait IdLoginPasswordJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val dataFormatILP = jsonFormat3(IdLoginPassword)
  }

  case class LoginPassword(login: String, password: String)
  trait LoginPasswordJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val dataFormatLP = jsonFormat2(LoginPassword)
  }

  val curLocalHost = "localhost"

  trait Constraint{
    def getSQLString(): String
  }

  case class LessEqualGreater(leg: String){
    private val logger = LoggerFactory.getLogger(classOf[RequestHandler])
    def getSignString(): String = leg.toLowerCase match {
      case "less" => "<"
      case "equal" => "="
      case "greater" => ">"
      case "less or equal" => "<="
      case "greater or equal" => ">="
      case _ => {
        logger.error("bad string in LessEqualGreater")
        ""
      }
    }
  }

  case class PriceConstraint(price: String, leg: LessEqualGreater) extends  Constraint{
    def getSQLString(): String = price match {
      case x if x.length > 0 =>s"AND price ${leg.getSignString()} $price"
      case _ => ""
    }
  }
  case class DateConstraint(date: String, leg: LessEqualGreater) extends Constraint{
    def getSQLString(): String ={
      s"AND date ${leg.getSignString()} '$date'"
    }
  }
  case class NameConstraint(name: String) extends  Constraint{
  def getSQLString(): String = name match{
      case "" => ""
      case _ => s"AND name = '$name'"
    }
  }

  case class ItemTypeConstraint(constraint: String) extends  Constraint{
    def getSQLString(): String = {
      if (constraint != "any") s"AND itemType = '$constraint'" else ""
    }
  }

  case class PlaceConstraint(place: String) extends  Constraint{
    def getSQLString(): String = place match{
      case "" => ""
      case _ => s"AND place = '$place'"
    }
  }

}
