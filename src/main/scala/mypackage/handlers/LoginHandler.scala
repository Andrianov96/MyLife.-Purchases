package mypackage.handlers


import akka.http.scaladsl.model.headers.HttpCookie
import mypackage.DBsupport._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives
import mypackage.CookiesSet
import mypackage.UsefulThings._
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol
import scala.util.Random

class LoginHandler(cookieSet: CookiesSet) extends Directives with IdLoginPasswordJsonSupport with LoginPasswordJsonSupport with ItemJsonSupport with ItemForSelectJsonSupport with SprayJsonSupport with DefaultJsonProtocol {
  private val logger = LoggerFactory.getLogger(classOf[LoginHandler])

  val route = path("login") {
    logger.debug("path login")
    entity(as[LoginPassword]) { json =>
      logger.debug(s"${json.login}  ${json.password}")
      //SELECT
      val Dao = new LoginPasswordDao
      val meetingPairs = Dao.findLoginPassword(json.login, json.password)
      if (meetingPairs.length > 1) {
        logger.error("more than one user has such login/password")
        complete("")
      }
      else if (meetingPairs.isEmpty) {
        logger.debug("emptyResult")
        complete(HttpResponse(entity = "http://" + curLocalHost + ":8080/loginError"))
      }
      else {
        logger.debug("matched user" + meetingPairs.head.toString)
        val rStr = Random.nextLong().toString
        logger.debug("cookie was deleted")
        setCookie(HttpCookie("userName", value = rStr)) {
          cookieSet.add(rStr, meetingPairs.head.id)
          logger.debug(s"cookie was setted $rStr -> ${meetingPairs.head.id} ")
          complete(HttpResponse(entity = "http://" + curLocalHost + ":8080/addorselect"))
        }
      }
    }
  }
}
