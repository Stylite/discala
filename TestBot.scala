package com.tripl3dogdare.discala

object TestBot {
  def main(args:Array[String]) {
    val bot = new Discala(MiscUtils.readFile(".auth")).login
  }
}