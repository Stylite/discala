package com.tripl3dogdare.discala

import scalaj.http.Http
import scala.concurrent._
import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext.Implicits.global
import io.circe._, io.circe.parser._, io.circe.Decoder, io.circe.generic.auto._

import JsonUtils._
import MiscUtils._

class Rest(val token:String) {
  private def send(path:String, method:String, data:String=null, query:Map[String, String]=Map()) = async {
    val qs = if(query.size > 0) "?"+query.map { case (k,v) => k+"="+v }.mkString("=") else ""
    val http = Http(Discala.API_URL+path+qs)
      .header("User-Agent", "DiscordBot (${Discala.WEBSITE}, ${Discala.VERSION})")
      .header("Authorization", "Bot $token")
      .method(method)
    if(data != null && data != "") http
      .header("Content-Type", "application/json")
      .charset("UTF-8")
      .postData(data)
    http.asString
  }
}