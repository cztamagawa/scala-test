package com.example.app.models

import java.sql.Connection

import org.joda.time.DateTime
import scalikejdbc._

/**
 * Created by a13902 on 6/10/15.
 */

case class User(id: Long, name: Option[String], email: Option[String], created_at: DateTime)

object User extends SQLSyntaxSupport[User] {

  override val tableName = "account"
  override val columns = Seq("id", "name", "email", "created_at")

  implicit val session = AutoSession
  var connection: Connection = null

  implicit def int2bool(i: Int) = if (i == 1) true else false

  
  def apply(u: ResultName[User])(rs: WrappedResultSet) = new User (
    rs.long(u.id), rs.stringOpt(u.name), rs.stringOpt(u.email), rs.jodaDateTime(u.created_at)
  )

  def all: List[User] = {
    val u = User.syntax("u")
    withSQL { select.from(User as u) }.map(User(u.resultName)).list.apply
  }

  def find(id: Int): Option[User] = {
    val u = User.syntax("u")
    withSQL { select.from(User as u).where.eq(u.id, id)}.map(User(u.resultName)).single.apply
  }

  def create(name: String, email: String): Unit = {
    val c = User.column
    withSQL {
      insert.into(User).namedValues(c.name -> name, c.email -> email, c.created_at -> DateTime.now)
    }.updateAndReturnGeneratedKey.apply
  }

  def doUpdate(id: Int, name: String, email: String): Boolean = {
    withSQL {
      update(User).set(
        User.column.name -> name,
        User.column.email -> email,
        User.column.created_at -> DateTime.now
      ).where.eq(User.column.id, id)
    }.update.apply: Boolean
  }

  def doDelete(id: Int): Boolean = {
    withSQL {
      delete.from(User).where.eq(User.column.id, id)
    }.update.apply: Boolean
  }
}