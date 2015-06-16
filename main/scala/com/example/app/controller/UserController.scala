package com.example.app.controller

import java.sql.Connection

import com.example.app.helper.ccToMap
import com.example.app.model.User
import org.joda.time.DateTime
import scalikejdbc._

/**
 * Created by a13902 on 6/10/15.
 */
class UserController {

  val url = "jdbc:mysql://localhost/scala_webapi"
  val username = "guest"
  val password = "pass"
  val created_at = "created_at"

  implicit val session = AutoSession
  var connection: Connection = null

  /**
   * establish ConnectionPool
   * default driver is MySQL
   * @param driver String
   */
  def establishConnectionPool(driver: String = "com.mysql.jdbc.Driver"): Unit = {
    Class.forName(driver)
    ConnectionPool.singleton(url, username, password)
  }

  /**
   * get all usernames on DB
   *
   * @return users List[Option[String]]
   */
  def selectAllUsers: List[Map[String, Any]] = {

    val u = User.syntax("u")
    val users = withSQL { select.from(User as u) }.map(User(u.resultName)).list.apply()
    val mappedUsers: List[Map[String, Any]] = users.map(u => ccToMap.apply(u)).map(u => u + (created_at -> u.get(created_at).get.toString))
    mappedUsers
  }


  /**
   * get user identified by id
   *
   * @param id Int
   * @return Option[User]
   */
  def selectUserById(id: Int): Option[Map[String, Any]] = {

    val u = User.syntax("u")
    val user = withSQL { select.from(User as u).where.eq(u.id, id)}.map(User(u.resultName)).single.apply()

    user match {
      case None => return None
      case Some(_) => return Option(getMappedUser(user))
    }

    None
  }

  /**
   * insert new user into DB
   *
   * @param name String
   * @param email String
   * @return
   */
  def insertUser(name: String, email: String) = {

    if (!name.isEmpty && !email.isEmpty) {
      val c = User.column
      withSQL {
        insert.into(User).namedValues(c.name -> name, c.email -> email, c.created_at -> DateTime.now)
      }.updateAndReturnGeneratedKey.apply()
    }
  }


  /**
   * update user info identified by id
   *
   * @param id Int
   * @param name String
   * @param email String
   */
  def updateUserById(id: Int, name: String, email: String): Int = {
    if(selectUserById(id).isEmpty) return 0

    withSQL {
      update(User).set(
        User.column.name -> name,
        User.column.email -> email,
        User.column.created_at -> DateTime.now
      ).where.eq(User.column.id, id)
    }.update.apply()

  }

  /**
   * delete user from data base identified by id
   *
   * @param id Int
   */
  def deleteUserById(id: Int): Int = {
    if(selectUserById(id).isEmpty) return 0

    withSQL {
      delete.from(User).where.eq(User.column.id, id)
    }.update.apply()

  }

  /**
   * convert case class User to Map format User w/ convert joda time to string
   *
   * @param user User
   * @return Map[String, Any]
   */
  private def getMappedUser(user: Option[User]) = {
    var mappedUser = ccToMap.apply(user.get)
    mappedUser = mappedUser + (created_at -> mappedUser.get(created_at).get.toString)
    mappedUser
  }
}
