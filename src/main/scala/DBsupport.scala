import scalikejdbc.{ConnectionPool, DB, DBSession, using}
import java.sql.Connection
import scalikejdbc._
import scalikejdbc.{ConnectionPool, DB, DBSession}
import scalikejdbc.config.DBs


object DBsupport {

  trait DbConnected {
    def connectionFromPool: Connection = ConnectionPool.borrow('demo_db) // (1)
    def dbFromPool: DB = DB(connectionFromPool) // (2)
    def insideLocalTx[A](sqlRequest: DBSession => A): A = { // (3)
      using(dbFromPool) { db =>
        db localTx { session =>
          sqlRequest(session)
        }
      }
    }

    def insideReadOnly[A](sqlRequest: DBSession => A): A = { // (4)
      using(dbFromPool) { db =>
        db readOnly { session =>
          sqlRequest(session)
        }
      }
    }
  }

  //table name
  class LoginPasswordDao extends DbConnected {
    def readAll(): List[IdLoginPassword] = {
      insideReadOnly { implicit session =>
        sql"SELECT * FROM MYTABLE".map(rs =>
          IdLoginPassword(
            rs.bigDecimal("id").toScalaBigDecimal,
            rs.string("login"),
            rs.string("password")
            )).list().apply()
      }
    }

    def findLoginPassword(login: String, password: String): List[IdLoginPassword] = {
      insideReadOnly { implicit session =>
        sql"SELECT * FROM MYTABLE WHERE login = $login AND password = $password".map(rs =>
          IdLoginPassword(
            rs.bigDecimal("id").toScalaBigDecimal,
            rs.string("login"),
            rs.string("password")
          )).list().apply()
      }
    }

  }

  class ItemDao extends  DbConnected {

  }

}
