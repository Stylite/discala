package com.tripl3dogdare.discala
import io.circe._, io.circe.parser._
import io.circe.optics.JsonPath.{root => root_}
import java.util.{Timer, TimerTask}

object JsonUtils {
  def root = root_
  def jsonFrom(raw:String) = new DSJson(parse(raw).getOrElse(Json.Null))

  implicit def dsj2json(dsj:DSJson):Json = dsj.json
  implicit def json2dsj(json:Json):DSJson = new DSJson(json)

  class DSJson(val json:Json) {
    import JsonUtils._
    def get[A](path:monocle.Optional[Json,A]):A = apply(path)
    def apply[A](path:monocle.Optional[Json,A]):A = path.getOption(this.json).getOrElse(null.asInstanceOf[A])
  }
}

object MiscUtils {
  def scheduleTask(f:() => Unit, at:Long, timer:Timer=new Timer()) = timer.schedule(wrapTimerTask(f), at)
  def wrapTimerTask(f:() => Unit):TimerTask = new TimerTask { def run = f() }

  def readFile(path:String) = {
    val source = scala.io.Source.fromFile(path)
    try source.mkString finally source.close
  }
}