import scalikejdbc.{ConnectionPool, DB, DBSession, using}
import java.sql.Connection

import UsefulThings.{ItemTypeConstraint, NameConstraint, PlaceConstraint, PriceConstraint}
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

  class PurchaseDao extends  DbConnected {
    def readAll(): List[Purchase] = {
      insideReadOnly { implicit session =>
        sql"SELECT * FROM PURCHASES".map(rs =>
          Purchase(
            rs.bigDecimal("id").toScalaBigDecimal,
            rs.bigDecimal("user_id").toScalaBigDecimal,
            rs.string("name"),
            rs.bigDecimal("price").toScalaBigDecimal,
            rs.string("date"),
            rs.string("place"),
            rs.string("itemType")
          )).list().apply()
      }
    }

    def insert(user_id: BigDecimal, name: String, price: Double, date: String, place: String, itemType: String) = {
      insideLocalTx { implicit session =>
        val strToExecute = sql"""INSERT INTO PURCHASES(user_id, name, price, date, place, itemtype)
          VALUES ($user_id, $name, $price, $date, $place, $itemType)"""
        strToExecute.execute().apply()
      }
    }

    def readAllForUser(user_id:BigDecimal,
                       name: NameConstraint, price: PriceConstraint,
                       date: String, place: PlaceConstraint,
                       itemType: ItemTypeConstraint):List[ItemToReceive] ={
      insideReadOnly { implicit session =>
        val st = SQLSyntax.createUnsafely(s"SELECT * FROM PURCHASES WHERE user_id = $user_id AND ${name.getSQLString()} ${price.getSQLString()} date = '$date' ${place.getSQLString()} ${itemType.getSQLString()}")
        sql"""$st""".map(rs =>
          ItemToReceive(
            rs.string("name"),
            rs.bigDecimal("price").toString,
            rs.string("date"),
            rs.string("place"),
            rs.string("itemType")
          )).list().apply()
      }
    }
  }

}
