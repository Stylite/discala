package com.tripl3dogdare.discala

import scalaj.http.Http
import com.neovisionaries.ws.client._

import JsonUtils._
import MiscUtils._

object Gateway {
  case class Connection(timer:java.util.Timer, var ws:WebSocket, var i:Long, var s:Int)
  private var connections = scala.collection.mutable.ListBuffer[Connection]()

  def connect(bot:Discala, handleDispatch:(String, DSJson) => Unit, resume:Connection) = {
    val gateway_res = Http("https://discordapp.com/api/v6/gateway").asString.body
    val gateway_url = jsonFrom(gateway_res)(root.url.string)+"/?v=6&encoding=json"

    val conn:Connection = 
      if(resume != null) {
        resume.ws = new WebSocketFactory().createSocket(gateway_url)
        resume
      } else Connection(
        new java.util.Timer(), 
        new WebSocketFactory().createSocket(gateway_url), 
        0, null.asInstanceOf[Int]
      )

    conn.ws.addListener(new WebSocketAdapter {
      override def onTextMessage(ws:WebSocket, msg:String) = {
        val json = jsonFrom(msg)
        if(json(root.s.int) != null.asInstanceOf[Int]) conn.s = json(root.s.int)

        val op = json(root.op.int)
        op match {
          case 0 => handleDispatch(json.get(root.t.string), json(root.d.json))
          case 1 => conn.ws heartbeat conn.s
          case 7 | 9 => reconnect(conn, bot, op == 7)

          case 10 => {
            conn.i = json(root.d.heartbeat_interval.int)
            conn.ws heartbeat conn.s
          }

          case 11 => scheduleTask(() => conn.ws heartbeat conn.s, conn.i, conn.timer) 
          case _ =>
        }
      }

      override def onConnected(ws:WebSocket, headers:java.util.Map[String, java.util.List[String]]) =
        println("Connected to Gateway service.")

      override def onDisconnected(ws:WebSocket, sf:WebSocketFrame, cf:WebSocketFrame, cbs:Boolean) = {
        if(sf != null) println(s"Disconnected with code ${sf.getCloseCode}: ${sf.getCloseReason}\nAttempting to reconnect...")
        else println("Disconnected.\nAttempting to reconnect...")
        reconnect(conn, bot, true)
      }
    })
    conn.ws.connect
    
    if(resume == null) conn.ws identify bot.token
    else conn.ws resume (bot.token, bot.session, conn.s)

    connections += conn
    conn
  }

  def close(conn:Connection) {
    connections -= conn
    conn.timer.cancel
    conn.timer.purge
    conn.ws.sendClose
    conn.ws.disconnect
  }
  def closeAll = connections.foreach { close _ }

  def reconnect(conn:Connection, bot:Discala, resume:Boolean=false) {
    close(conn)
    Thread.sleep(1500)
    bot.login(if(resume) conn else null)
  }
  
  implicit class WebSocketOps(val ws:WebSocket) {
    def !(msg:String) = ws sendText msg

    def identify(token:String) = ws ! s"""
      {
        "op": 2,
        "d": {
          "token": "$token",
          "properties": {
            "$$os": "${System.getProperty("os.name")}",
            "$$browser": "discala",
            "$$device": "discala"
          },
          "compress": false,
          "large_threshold": 250,
          "shard": [0, 1]
        }
      }
    """
    def resume(token:String, session:String, s:Int) = ws ! s"""
      {
        "op": 6,
        "d": {
          "token": "$token",
          "session_id": "$session",
          "seq": $s
        }
      }
    """
    def heartbeat(s:Int) = ws ! s"""{"op":1,"d":$s}"""
  }

}