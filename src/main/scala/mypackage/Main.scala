package mypackage

import java.sql.Connection

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import handlers.RequestHandler
import mypackage.UsefulThings._
import scalikejdbc.ConnectionPool
import scalikejdbc.config.DBs


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
                " price NUMBER NOT NULL, date DATE, place VARCHAR(255), itemtype VARCHAR(255), PRIMARY KEY (id), FOREIGN KEY (user_id) REFERENCES MYTABLE(id))"
    stmt.executeUpdate(sql)
    stmt.executeUpdate(sql2)
    stmt.executeUpdate("INSERT INTO MYTABLE(login, password) " +
      s"VALUES ('loginv', 'passwordv')")
    stmt.executeUpdate("INSERT INTO MYTABLE(login, password) " +
      s"VALUES ('loginv1', 'passwordv1')")
    //DB initiation block

    val myRequestHandler = new RequestHandler
    val bindingFuture = Http().bindAndHandle(myRequestHandler.route, curLocalHost, 8080)
    println(s"Server online at http://" + curLocalHost + ":8080/\nPress RETURN to stop...")
    var s = scala.io.StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

}
}