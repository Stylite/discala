package com.tripl3dogdare.discala
package testbot

import io.circe.literal._, io.circe.generic.auto._
import data._

object TestBot {
  def main(args:Array[String]) {
    val bot = new Discala().login(readFile(".auth"))
  }
}