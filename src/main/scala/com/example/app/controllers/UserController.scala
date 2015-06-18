package com.example.app.controllers

import com.example.app.db.DBContext
import org.json4s.ext.JodaTimeSerializers
import org.scalatra._
import scalate.ScalateSupport
import org.json4s.{DefaultFormats, Formats, ext}
import org.scalatra.json._
import com.example.app.models._
/**
 * Created by a13902 on 6/11/15.
 */
class UserController extends ScalatraServlet with ScalateSupport with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats ++ JodaTimeSerializers.all

  before() {
    DBContext.establishConnectionPool()
    contentType = formats("json")
  }

  /**
   * show HTML website content
   */
  get("/") {
    contentType = "text/html"
    ssp("index.ssp", "layout" -> "")
  }

  /**
   * show JSON format dataset
   */
  get("/users") {
    User.all
  }

  /**
   * show single user data in JSON format
   */
  get("/users/:id") {
    User.find(params("id").toInt) map(r => Ok(r)) getOrElse NotFound
  }

  /**
   * create new user
   */
  post("/users") {
    User.create(params("name"), params("email"))
  }

  /**
   * update a preexisting user by id
   */
  put("/users/:id") {
    if (User.doUpdate(params("id").toInt, params("name"), params("email"))) Ok
    else NotFound
  }

  /**
   * delete a preexisting user by id
   */
  delete("/users/:id") {
    if (User.doDelete(params("id").toInt)) Ok
    else NotFound
  }

}
