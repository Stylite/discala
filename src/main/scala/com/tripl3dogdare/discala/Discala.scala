package com.tripl3dogdare.discala

import Gateway.{WebSocketOps, Connection}
import JsonUtils._
import MiscUtils._

class Discala(val token:String) {
  private var conn:Connection = null
  var session:String = ""

  private var _user:User = null
  def user = _user

  private var _guilds = scala.collection.mutable.Map[String, Either[UnavailableGuild, Guild]]()
  def guilds = _guilds.filter{ case (k,v) => v.isRight }.mapValues{v => v.right.get}
  def unavailableGuilds = _guilds.filter{ case (k,v) => v.isLeft }.mapValues{v => v.left.get}

  def login:Discala = login(null)
  def login(resume:Connection=null):Discala = {
    if(conn != null) Gateway.close(conn)
    conn = Gateway.connect(this, handleDispatch _, resume)
    this
  }

  private def handleDispatch(event:String, data:DSJson) = event match {
    case _ => println(event)
  }
}