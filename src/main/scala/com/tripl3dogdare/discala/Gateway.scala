package com.tripl3dogdare.discala

import scalaj.http.Http
import com.neovisionaries.ws.client._

import JsonUtils._
import MiscUtils._

object Gateway {
  type Connection = (java.util.Timer, WebSocket)
  private var connections = scala.collection.mutable.ListBuffer[Connection]()

  def connect(bot:Discala, handleDispatch:(String, DSJson) => Unit) = {
    val gateway_res = Http("https://discordapp.com/api/v6/gateway").asString.body
    val gateway_url = jsonFrom(gateway_res)(root.url.string)+"/?v=6&encoding=json"

    var hbeat_t = new java.util.Timer()
    var hbeat_s:Int = null.asInstanceOf[Int]
    var hbeat_i:Long = 0

    val ws = new WebSocketFactory().createSocket(gateway_url)
    var conn:Connection = null
    ws.addListener(new WebSocketAdapter {
      override def onTextMessage(ws:WebSocket, msg:String) = {
        val json = jsonFrom(msg)
        if(json(root.s.int) != null.asInstanceOf[Int]) hbeat_s = json(root.s.int)

        json(root.op.int) match {
          case 0 => handleDispatch(json.get(root.t.string), json(root.d.json))
          case 1 => ws heartbeat hbeat_s
          case 7 | 9 => {
            close(conn)
            bot.login
          }
          case 10 => {
            hbeat_i = json(root.d.heartbeat_interval.int)
            ws heartbeat hbeat_s
          }
          case 11 => scheduleTask(() => ws heartbeat hbeat_s, 1000, hbeat_t)
          case _ =>
        }
      }
    })
    ws.connect
    ws identify bot.token

    conn = (hbeat_t, ws)
    connections += conn
    conn
  }

  def close(conn:Connection) {
    connections -= conn
    conn._1.purge
    conn._2.sendClose
    conn._2.disconnect
  }
  def closeAll = connections.foreach { close _ }
  
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
    def heartbeat(s:Int) = ws ! s"""{"op":1,"d":$s}"""
  }

}