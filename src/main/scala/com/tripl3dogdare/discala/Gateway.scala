package com.tripl3dogdare.discala

import scalaj.http.Http
import com.neovisionaries.ws.client._
import io.circe._, io.circe.generic.auto._

class Gateway(val token:String, val dispatchHandler:(Symbol, Json) => Unit) {
  private val gateway_res = Http(Discala.API_URL+"gateway").asString.body
  private val gateway_url = jsonFrom(gateway_res)(root.url.string)+"/?v=6&encoding=json"

  private var hbseq = 0
  private var session = ""
  private var lastack = true
  private var ws:WebSocket = null
  private var timer = new java.util.Timer

  def connect = {
    if(ws != null) ws.disconnect
    ws = new WebSocketFactory().createSocket(gateway_url)
    ws.addListener(new WebSocketAdapter {
      override def onTextMessage(ws:WebSocket, msg:String) = onMessage(jsonFrom(msg))
      override def onDisconnected(ws:WebSocket, sf:WebSocketFrame, cf:WebSocketFrame, cbs:Boolean) = onDisconnect(sf)
    })

    print("Connecting... ")
    try { ws.connect; println("Success!") } catch { case e:WebSocketException => println("Failed, trying again soon.") }
    ws.isOpen
  }

  def login:Gateway = reconnect(false)
  def reconnect:Gateway = reconnect(true)
  def reconnect(resume:Boolean) = {
    if(connect) {
      lastack = true

      if(resume) ws ! pkt_resume
      else ws ! pkt_identify
    }
    this
  }

  private def onMessage(msg:Json) {
    val gwm = msg.as[GatewayMessage].right.get
    if(gwm.seq != hbseq) hbseq = gwm.seq
    if(gwm.opc == DISPATCH && gwm.typ == 'READY) session = gwm.dat(root.session_id.string)

    gwm.opc match {
      case DISPATCH => dispatchHandler(gwm.typ, gwm.dat)
      case RECONNECT => reconnect
      case INVALID_SESSION => { Thread.sleep(1500); reconnect(gwm.dat.asBoolean.get) }

      case HELLO => {
        timer.cancel
        timer = new java.util.Timer
        timer.setInterval(() => {
          if(!lastack) reconnect
          else {
            ws ! pkt_heartbeat
            lastack = false
          }
        }, gwm.dat(root.heartbeat_interval.long))
      }

      case HEARTBEAT => ws ! pkt_heartbeat
      case HEARTBEAT_ACK => lastack = true
      case op => println("Recieved invalid Gateway opcode $op, ignoring.")
    }
  }

  private def onDisconnect(sf:WebSocketFrame) {
    if(sf != null) println(s"Disconnected with code ${sf.getCloseCode}: ${sf.getCloseReason}")
    else println("Disconnected.")
  }

  case class GatewayMessage(
    op:Int,
    d:Json,
    s:Option[Int],
    t:Option[String]
  ) {
    def opc = op
    def dat = d
    def seq = s.getOrElse(-1)
    def typ = Symbol(t.getOrElse("NULL"))

    override def toString = 
      s"GatewayMessage(opc => $opc, dat => $dat, seq => $seq, typ => $typ)"
  }

  // Opcodes
  final val DISPATCH = 0
  final val HEARTBEAT = 1
  final val IDENTIFY = 2
  final val STATUS_UPDATE = 3
  final val VOICE_STATE_UPDATE = 4
  final val VOICE_SERVER_PING = 5
  final val RESUME = 6
  final val RECONNECT = 7
  final val REQUEST_GUILD_MEMBERS = 8
  final val INVALID_SESSION = 9
  final val HELLO = 10
  final val HEARTBEAT_ACK = 11

  // Pre-defined packets
  final val pkt_identify = s"""{
    "op": $IDENTIFY,
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
  }"""
  def pkt_heartbeat = s"""{"op":$HEARTBEAT,"d":$hbseq}"""
  def pkt_resume = s"""{ "op": $RESUME, "d": { "token": "$token", "session_id": "$session", "seq": $hbseq }}"""
  
  implicit class WebSocketOps(val ws:WebSocket) { def !(msg:String) = ws sendText msg }
  // private[discala] def !(msg:String) = ws ! msg
}
