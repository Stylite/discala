package com.tripl3dogdare.discala

import scalaj.http.Http
import scala.concurrent._
import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext.Implicits.global
import io.circe._, io.circe.parser._, io.circe.Decoder, io.circe.generic.auto._

class Rest(val token:String) {
  type Query = Map[String, String]

  private def send(path:String, method:String, data:String=null, query:Query=Map()) = async {
    val qs = if(query.size > 0) "?"+query.map { case (k,v) => k+"="+v }.mkString("=") else ""
    val http = Http(Discala.API_URL+path+qs)
      .header("User-Agent", s"DiscordBot (${Discala.WEBSITE}, ${Discala.VERSION})")
      .header("Authorization", s"Bot $token")
      .header("Content-Type", "application/json")
      .charset("UTF-8")
      .method(method)
    if(data != null && data != "") http.postData(data)
    http.asString
  }

  private[discala] def get(path:String, query:Query=Map()) = send(path, "get", query=query)
  private[discala] def head(path:String, query:Query=Map()) = send(path, "head", query=query)

  private[discala] def post(path:String, data:String=null) = send(path, "post", data=data)
  private[discala] def put(path:String, data:String=null) = send(path, "put", data=data)
  private[discala] def delete(path:String, data:String=null) = send(path, "delete", data=data)
}