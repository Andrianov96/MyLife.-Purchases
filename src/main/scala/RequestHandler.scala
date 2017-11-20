import DBsupport.LoginPasswordDao
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.headers.HttpCookie
import akka.http.scaladsl.server.Directives
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol

import scala.util.Random

class RequestHandler extends Directives  with SprayJsonSupport with DefaultJsonProtocol with IdLoginPasswordJsonSupport with LoginPasswordJsonSupport with ItemJsonSupport {
  private val logger = LoggerFactory.getLogger(classOf[RequestHandler])
  var cookieSet = CookiesSet(Map())

  val route =
    cors() {
      get {
        pathSingleSlash {
          setCookie(HttpCookie("userName", value = "paul")) {
            logger.debug("get method")
            getFromFile("src/main/resources/ShowCookie.html")
          }
        } ~
          path("cookie") {
            cookie("userName"){ cookieName =>
              complete(s"The logged in user is '${cookieName.value}'")
            }
          } ~
          path("additem") {
            cookie("userName"){ cookieName =>
              if (cookieSet.contains(cookieName.value)) {
                getFromFile("src/main/resources/AddItem.html")
              } else {
                complete(s"Such cookie is not contained")
              }
            }
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
                complete("")
              }
              else {
                logger.debug("matched user" + meetingPairs.head.toString)
                val rStr = Random.nextLong().toString
                deleteCookie("userName") {
                  logger.debug("cookie was deleted")
                  setCookie(HttpCookie("userName", value = rStr)) {
                    cookieSet = cookieSet.add(rStr, meetingPairs.head.id)
                    logger.debug(s"cookie was setted $rStr -> ${meetingPairs.head.id} ")
                    complete(HttpResponse(entity = "http://localhost:8080/additem"))
                  }
                }
              }
            }
          } ~
          path("addDefiniteItem") {
            logger.debug("path addDefiniteItem")
            entity(as[ItemToReceive]) { json =>
              logger.debug(s"received item - ${json.name} ${json.price} ${json.date} ${json.place} ${json.itemType}")

              complete("Item was received")
            }
          }
        }
    }

}
