import DBsupport.{LoginPasswordDao, PurchaseDao}
import UsefulThings._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpResponse, ResponseEntity}
import akka.http.scaladsl.model.headers.HttpCookie
import akka.http.scaladsl.server.Directives
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol
import spray.json._

import scala.util.Random

class RequestHandler extends Directives with IdLoginPasswordJsonSupport with LoginPasswordJsonSupport with ItemJsonSupport with ItemForSelectJsonSupport with SprayJsonSupport with DefaultJsonProtocol{
  private val logger = LoggerFactory.getLogger(classOf[RequestHandler])
  var cookieSet = CookiesSet(Map())

  val route =
    cors() {
      get {
        logger.debug("get method")
        pathSingleSlash {
          setCookie(HttpCookie("userName", value = "paul")) {
            logger.debug("get method")
            getFromFile("src/main/resources/html/ShowCookie.html")
          }
        } ~
          path("cookie") {
            cookie("userName") { cookieName =>
              complete(s"The logged in user is '${cookieName.value}'")
            }
          } ~
          path("additem") {
            cookie("userName") { cookieName =>
              if (cookieSet.contains(cookieName.value))
                getFromFile("src/main/resources/html/AddItem.html")
              else
                complete(s"src/main/resources/html/WrongCookie.html")
            }
          } ~
          path("wrongcookie") {
            getFromFile("src/main/resources/html/WrongCookie.html")
          } ~
          path("addorselect") {
            getFromFile("src/main/resources/html/AddOrSelect.html")
          } ~
          path("selectItem") {
            getFromFile("src/main/resources/html/SelectItem.html")
          } ~
          path("loginError") {
            getFromFile("src/main/resources/html/LoginError.html")
          } ~
          path("loginPage") {
            getFromFile("src/main/resources/html/LoginPage.html")
          }
      } ~
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
                complete(HttpResponse(entity = "http://localhost:8080/loginError"))
              }
              else {
                logger.debug("matched user" + meetingPairs.head.toString)
                val rStr = Random.nextLong().toString
                  logger.debug("cookie was deleted")
                  setCookie(HttpCookie("userName", value = rStr)) {
                    cookieSet = cookieSet.add(rStr, meetingPairs.head.id)
                    logger.debug(s"cookie was setted $rStr -> ${meetingPairs.head.id} ")
                    complete(HttpResponse(entity = "http://localhost:8080/addorselect"))
                  }
              }
            }
          } ~
            path("addDefiniteItem") {
              logger.debug("path addDefiniteItem")
              cookie("userName") { cookieName =>
                if (cookieSet.contains(cookieName.value))
                  entity(as[ItemToReceive]) { json =>
                    logger.debug(s"received item - ${json.name} ${json.price} ${json.date} ${json.place} ${json.itemType}")
                    val purchaseDao = new PurchaseDao
                    purchaseDao.insert(cookieSet.getId(cookieName.value), json.name, json.price.toDouble, json.date, json.place, json.itemType)
                    complete(HttpResponse(entity = "http://localhost:8080/addorselect"))
                  }
                else
                  complete(HttpResponse(entity = "http://localhost:8080/wrongcookie"))
              }
            }  ~
            path("selectDefiniteItem"){
              logger.debug("path selectDefiniteItem")
              cookie("userName") { cookieName =>
                if (cookieSet.contains(cookieName.value))
                  entity(as[ItemForSelect]) { json =>
                    logger.debug(s"received item - ${json.name} ${json.priceLEG} ${json.price} ${json.date} ${json.place} ${json.itemType}")
                    val purchaseDao = new PurchaseDao
                    val res = purchaseDao.readAllForUser(cookieSet.getId(cookieName.value),
                      NameConstraint(json.name),
                      PriceConstraint(json.price, LessEqualGreater(json.priceLEG)),
                      json.date,
                      PlaceConstraint(json.place),
                      ItemTypeConstraint(json.itemType)).toArray
                    res.foreach(s=> logger.debug("selected item" + s.toString))

                    var entity2: ResponseEntity = res.toJson.compactPrint
                    entity2 = entity2.withContentType(ContentTypes.`application/json`)
                    complete(HttpResponse(entity = entity2))
                  }
                else
                  complete(HttpResponse(entity = "http://localhost:8080/wrongcookie"))
              }
            }
        }
    }
}


