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
    var sql = "CREATE TABLE MYTABLE( id NUMBER NOT NULL AUTO_INCREMENT, login VARCHAR(255), password VARCHAR(255), PRIMARY KEY (id))"
    var sql2 = "CREATE TABLE PURCHASES(id NUMBER NOT NULL AUTO_INCREMENT, user_id NUMBER NOT NULL, name VARCHAR(255)," +
                " price NUMBER NOT NULL, date VARCHAR(255), place VARCHAR(255), itemtype VARCHAR(255), PRIMARY KEY (id), FOREIGN KEY (user_id) REFERENCES MYTABLE(id))"
    stmt.executeUpdate(sql)
    stmt.executeUpdate(sql2)
    stmt.executeUpdate("INSERT INTO MYTABLE(login, password) " +
      s"VALUES ('loginv', 'passwordv')")
    stmt.executeUpdate("INSERT INTO MYTABLE(login, password) " +
      s"VALUES ('loginv1', 'passwordv1')")
    //DB initiation block

    val myRequestHandler = new RequestHandler
    val bindingFuture = Http().bindAndHandle(myRequestHandler.route, "localhost", 8080)
    println(s"Server online at http://localhost.com:8080/\nPress RETURN to stop...")
    var s = scala.io.StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

}
}