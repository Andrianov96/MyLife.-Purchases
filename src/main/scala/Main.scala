import java.sql.{Connection, DriverManager}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import java.sql.{Connection, DriverManager}

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.headers.HttpCookie
import spray.json._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import scalikejdbc._
import scalikejdbc.{ConnectionPool, DB, DBSession}
import scalikejdbc.config.DBs
import com.typesafe.scalalogging._
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object WebServer1 {
  def main(args: Array[String]) {
    //AKKA
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher
    //AKKA
    DBs.setup('demo_db)

    def connectionFromPool: Connection = ConnectionPool.borrow('demo_db)
    var stmt = connectionFromPool.createStatement
    var sql = "CREATE TABLE MYTABLE " + "( login VARCHAR(255), " + " password VARCHAR(255))"
    stmt.executeUpdate(sql)
    sql =
    //DB initiation block

    val myRequestHandler = new RequestHandler
    val bindingFuture = Http().bindAndHandle(myRequestHandler.route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    var s = scala.io.StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

}
}