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
  private val selectItem = new SelectItemHandler(cookieSet)
  private val loginHandler = new LoginHandler(cookieSet)

  val route =
    cors() {
      getHandler.route ~
        post {
          logger.debug("post method")
          loginHandler.route ~
            addItemHandler.route  ~
            selectItem.route
        }
    }
}


