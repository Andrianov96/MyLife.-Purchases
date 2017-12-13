package mypackage.handlers

import mypackage.DBsupport._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpResponse, ResponseEntity}
import akka.http.scaladsl.model.headers.HttpCookie
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import mypackage.CookiesSet
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol
import spray.json._
import mypackage.UsefulThings._

import scala.util.Random
class RequestHandler extends Directives with IdLoginPasswordJsonSupport with LoginPasswordJsonSupport with ItemJsonSupport with ItemForSelectJsonSupport with SprayJsonSupport with DefaultJsonProtocol{
  private val logger = LoggerFactory.getLogger(classOf[RequestHandler])
  var cookieSet = new CookiesSet(Map())

  private val getHandler = new GetHandler(cookieSet)
  private val addItemHandler = new AddItemHandler(cookieSet)
  val route =
    cors() {
      getHandler.route ~
        post {
          logger.debug("post method")
          path("login") {
            logger.debug("path login")
            entity(as[LoginPassword]) { json =>
              logger.debug(s"${json.login}  ${json.password}")
              //SELECT
              val Dao = new LoginPasswordDao
              val meetingPairs = Dao.findLoginPassword(json.login, json.password)
              if (meetingPairs.length > 1) {
                logger.debug("more than one user has such login/password")
                complete("")
              }
              else if (meetingPairs.isEmpty) {
                logger.debug("emptyResult")
                complete(HttpResponse(entity = "http://" + curLocalHost + ":8080/loginError"))
              }
              else {
                logger.debug("matched user" + meetingPairs.head.toString)
                val rStr = Random.nextLong().toString
                //deleteCookie("userName") {
                  logger.debug("cookie was deleted")
                  setCookie(HttpCookie("userName", value = rStr)) {
                    cookieSet.add(rStr, meetingPairs.head.id)
                    logger.debug(s"cookie was setted $rStr -> ${meetingPairs.head.id} ")
                    complete(HttpResponse(entity = "http://" + curLocalHost + ":8080/addorselect"))
                  }
                //}
              }
            }
          } ~
            addItemHandler.route  ~
            path("selectDefiniteItem"){
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
    }
}

