package mypackage.handlers


import akka.http.scaladsl.model.{ContentTypes, ResponseEntity}
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives
import mypackage.CookiesSet
import mypackage.DBsupport.PurchaseDao
import mypackage.UsefulThings._
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol

class SelectItemHandler(cookieSet: CookiesSet) extends Directives with IdLoginPasswordJsonSupport with LoginPasswordJsonSupport with ItemJsonSupport with ItemForSelectJsonSupport with SprayJsonSupport with DefaultJsonProtocol {
  private val logger = LoggerFactory.getLogger(classOf[SelectItemHandler])

  val route = path("selectDefiniteItem"){
    logger.debug("path selectDefiniteItem")
    cookie("userName") { cookieName =>
      if (cookieSet.contains(cookieName.value))
        entity(as[ItemForSelect]) { json =>
          logger.debug(s"received item - ${json.name} ${json.priceLEG} ${json.price} ${json.dateLEG} ${json.date} ${json.place} ${json.itemType}")
          val purchaseDao = new PurchaseDao
          val res = purchaseDao.readAllForUser(cookieSet.getId(cookieName.value),
            NameConstraint(json.name),
            PriceConstraint(json.price, LessEqualGreater(json.priceLEG)),
            DateConstraint(json.date, LessEqualGreater(json.dateLEG)),
            PlaceConstraint(json.place),
            ItemTypeConstraint(json.itemType)).toArray
          res.foreach(s=> logger.debug("selected item" + s.toString))

          var entity2: ResponseEntity = res.toJson.compactPrint
          entity2 = entity2.withContentType(ContentTypes.`application/json`)
          complete(HttpResponse(entity = entity2))
        }
      else
        complete(HttpResponse(entity = "http://" + curLocalHost + ":8080/wrongcookie"))
    }
  }
}
