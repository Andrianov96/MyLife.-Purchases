package mypackage.handlers

import akka.http.scaladsl.server.Directives
import mypackage.CookiesSet
import org.slf4j.LoggerFactory

class GetHandler(cookieSet: CookiesSet) extends Directives{
  private val logger = LoggerFactory.getLogger(classOf[GetHandler])

  val route = get {
    logger.debug("get method")
    pathSingleSlash {
      logger.debug("single slash")
      getFromFile("src/main/resources/html/LoginPage.html")
    } ~
      path("additem") {
        cookie("userName") { cookieName =>
          if (cookieSet.contains(cookieName.value))
            getFromFile("src/main/resources/html/AddItem.html")
          else {
            logger.debug(s"wrongcookie = ${cookieName.value}")
            getFromFile("src/main/resources/html/WrongCookie.html")
          }
        } ~
          getFromFile("src/main/resources/html/WrongCookie.html")
      } ~
      path("wrongcookie") {
        cookie("userName") { cookie =>
          logger.debug(s"wrongcookie = ${cookie.value}")
          getFromFile("src/main/resources/html/WrongCookie.html")
        }
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
  }
}
