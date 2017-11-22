import org.slf4j.LoggerFactory
import scalikejdbc._

object UsefulThings {

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

  case class PriceConstraint(price: Double, leg: LessEqualGreater) extends  Constraint{
    def getSQLString(): String ={
        s"price ${leg.getSignString()} $price AND"
    }
  }
//  case class DateConstraint(date: String, leg: LessEqualGreater) extends Constraint{
//    def getSQLString(): String ={
//      s"WHERE date ${leg.getSignString()} ${price}"
//    }
//  }
  case class NameConstraint(name: String) extends  Constraint{
  def getSQLString(): String = {
    name match{
      case "" => ""
      case _ => s"name = '$name' AND"
    }
  }
}

  case class ItemTypeConstraint(constraint: String) extends  Constraint{
    def getSQLString(): String = {
      if (constraint != "any") s"AND itemType = '$constraint'" else ""
    }
  }

}
