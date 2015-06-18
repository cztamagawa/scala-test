package com.example.app.db

import com.typesafe.config.ConfigFactory
import scalikejdbc.ConnectionPool

/**
 * Created by a13902 on 6/17/15.
 */
object DBContext {
  def establishConnectionPool(): Unit = {
    val conf = ConfigFactory.load()

    val db_driver = conf.getString("db.default.driver")
    val db_type = conf.getString("db.default.type")
    val db_server = conf.getString("db.default.server")
    val db_schema = conf.getString("db.default.schema")
    val db_user = conf.getString("db.default.user")
    val db_pass = conf.getString("db.default.pass")

    val db_uri = "jdbc:" + db_type + "://" + db_server + "/" + db_schema

    Class.forName(db_driver)
    ConnectionPool.singleton(db_uri, db_user, db_pass)
  }
}
