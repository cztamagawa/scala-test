package com.example.app.helper

import com.example.app.model.User

/**
 * Created by a13902 on 6/11/15.
 */
object ccToMap {
  /**
   * convert case class format to Map format
   *
   * @param user User
   * @return Map[String, Any]
   */
  def apply(user: User): Map[String, Any] = {
    user.getClass.getDeclaredFields.map(_.getName).zip(user.productIterator.toList).toMap
  }
}
