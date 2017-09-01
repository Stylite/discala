package com.tripl3dogdare.discala

import Gateway.{WebSocketOps, Connection}
import JsonUtils._
import MiscUtils._

class Discala(val token:String) {
  private var conn:Connection = null

  def login:Discala = {
    if(conn != null) Gateway.close(conn)
    conn = Gateway.connect(this, handleDispatch _)
    this
  }

  private def handleDispatch(event:String, data:DSJson) = event match {
    case _ => println(event)
  }
}