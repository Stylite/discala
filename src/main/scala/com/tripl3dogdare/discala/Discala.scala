package com.tripl3dogdare.discala

import io.circe._, io.circe.parser._, io.circe.Decoder, io.circe.generic.auto._
import JsonUtils._
import MiscUtils._

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