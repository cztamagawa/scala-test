package com.example.app.model

import org.joda.time.DateTime
import scalikejdbc._

/**
 * Created by a13902 on 6/10/15.
 */

case class User(id: Long, name: Option[String], email: Option[String], created_at: DateTime)

object User extends SQLSyntaxSupport[User] {
  override val tableName = "account"
  override val columns = Seq("id", "name", "email", "created_at")

  def apply(u: ResultName[User])(rs: WrappedResultSet) = new User (
    rs.long(u.id), rs.stringOpt(u.name), rs.stringOpt(u.email), rs.jodaDateTime(u.created_at)
  )

}