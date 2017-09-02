package com.tripl3dogdare.discala
package testbot

object TestBot {
  def main(args:Array[String]) {
    val bot = new Discala().login(MiscUtils.readFile(".auth"))
  }
}