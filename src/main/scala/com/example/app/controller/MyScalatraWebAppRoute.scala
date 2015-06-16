package com.example.app.controller

import com.example.app.MyScalatraWebAppStack
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.json4s.ext.{DateTimeSerializer, DateParser, JodaTimeSerializers}
import org.scalatra._
import scalate.ScalateSupport
import org.json4s.{DefaultFormats, Formats, ext}
import org.scalatra.json._
import com.example.app.controller._
import com.example.app.model._
/**
 * Created by a13902 on 6/11/15.
 */
class MyScalatraWebAppRoute extends ScalatraServlet with ScalateSupport with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats
  val userController: UserController = new UserController

  before() {
    userController.establishConnectionPool()
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
    userController.selectAllUsers
  }

  /**
   * show single user data in JSON format
   */
  get("/users/:id") {

    val result = userController.selectUserById(params("id").toInt)

    result match {
      case None => NotFound("sorry, the id could not be found")
      case _ => Ok(result)
    }
  }

  /**
   * create new user
   */
  post("/users") {
    userController.insertUser(params("name"), params("email"))
  }

  /**
   * update a preexisting user by id
   */
  put("/users/:id") {
    val isFound = userController.updateUserById(params("id").toInt, params("name"), params("email"))

    isFound match {
      case 0 => NotFound("sorry, the id could not be found")
      case 1 => Ok("successfully inserted into database")
    }
  }

  /**
   * delete a preexisting user by id
   */
  delete("/users/:id") {
    val isFound = userController.deleteUserById(params("id").toInt)

    isFound match {
      case 0 => NotFound("sorry, the id could not be found")
      case 1 => Ok("successfully deleted the data")
    }
  }


}
