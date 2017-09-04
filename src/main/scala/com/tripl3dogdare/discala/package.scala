package com.tripl3dogdare
package object discala {
  import io.circe._, io.circe.parser._
  import io.circe.optics.JsonPath.{root => root_}
  import io.circe.Decoder.Result

  def root = root_
  def jsonFrom(raw:String) = new DSJson(parse(raw).getOrElse(Json.Null))

  implicit def dsj2json(dsj:DSJson):Json = dsj.json
  implicit def json2dsj(json:Json):DSJson = new DSJson(json)

  class DSJson(val json:Json) {
    def get[A](path:monocle.Optional[Json,A]):A = apply(path)
    def apply[A](path:monocle.Optional[Json,A]):A = path.getOption(this.json).getOrElse(null.asInstanceOf[A])
  }
  
  implicit class NullOption[A](opt:Option[A]) { def ? :A = opt.getOrElse(null.asInstanceOf[A]) }
  implicit class CirceResult[_,A](eith:Either[_,A]) { def ? :A = eith.right.getOrElse(null.asInstanceOf[A]) }

  import java.util.{Timer, TimerTask}
  implicit class TimerOps(timer:Timer) {
    def setTimeout(f:() => Unit, time:Long) = timer.schedule(wrapTimerTask(f), time)
    def setInterval(f:() => Unit, time:Long) = timer.schedule(wrapTimerTask(f), time, time)
    def wrapTimerTask(f:() => Unit):TimerTask = new TimerTask { def run = f() }
  }
  
  def readFile(path:String) = {
    val source = scala.io.Source.fromFile(path)
    try source.mkString finally source.close
  }
}