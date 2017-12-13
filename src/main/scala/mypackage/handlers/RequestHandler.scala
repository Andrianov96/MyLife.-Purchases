package mypackage.handlers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import mypackage.CookiesSet
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol
import mypackage.UsefulThings._

class RequestHandler extends Directives {
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


