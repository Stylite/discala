package com.tripl3dogdare.discala

import io.circe._

class Discala {
  private var gateway:Gateway = null

  def login(token:String) = {
    gateway = new Gateway(token, handleDispatch _).login
    this
  }

  private def handleDispatch(event:Symbol, data:Json) = event match {
    case _ => println(event)
  }
}

object Discala {
  final val WEBSITE = "https://github.com/tripl3dogdare/discala"
  final val VERSION = "0.0.1a"
  final val API_URL = "https://discordapp.com/api/v6/"
  final val AVI_URL = "https://cdn.discordapp.com/"
}