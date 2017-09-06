package com.tripl3dogdare.discala
package data

case class Channel(
  id:String,
  `type`:Int,
  guild_id:Option[String],
  position:Option[Int],
  permission_overwrites:Option[List[Channel.PermissionOverwrite]],
  name:Option[String],
  topic:Option[String],
  last_message_id:Option[String],
  bitrate:Option[Int],
  user_limit:Option[Int],
  recipients:Option[List[User]],
  icon:Option[String],
  owner_id:Option[String],
  application_id:Option[String]
)

object Channel {
  case class PermissionOverwrite(
    id:String,
    `type`:String,
    allow:Int,
    deny:Int
  )
}