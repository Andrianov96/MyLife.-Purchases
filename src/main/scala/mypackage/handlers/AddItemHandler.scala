package mypackage.handlers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives
import mypackage.CookiesSet
import mypackage.DBsupport.PurchaseDao
import mypackage.UsefulThings._
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol


class AddItemHandler(val cookieSet: CookiesSet) extends Directives with ItemJsonSupport with ItemForSelectJsonSupport with SprayJsonSupport with DefaultJsonProtocol{
  private val logger = LoggerFactory.getLogger(classOf[AddItemHandler])
  val route = path("addDefiniteItem") {
    logger.debug("path addDefiniteItem")
    cookie("userName") { cookieName =>
      if (cookieSet.contains(cookieName.value))
        entity(as[ItemToReceive]) { json =>
          logger.debug(s"received item - ${json.name} ${json.price} ${json.date} ${json.place} ${json.itemType}")
          val purchaseDao = new PurchaseDao
          purchaseDao.insert(cookieSet.getId(cookieName.value), json.name, json.price.toDouble, json.date, json.place, json.itemType)
          complete(HttpResponse(entity = "http://" + curLocalHost + ":8080/addorselect"))
        }
      else
        complete(HttpResponse(entity = "http://" + curLocalHost + ":8080/wrongcookie"))
    }
  }
}
