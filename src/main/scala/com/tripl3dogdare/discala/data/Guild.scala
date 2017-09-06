package com.tripl3dogdare.discala
package data

case class Guild(
  id:String,
  name:String,
  icon:String,
  splash:String,
  owner_id:String,
  region:String,
  afk_channel_id:String,
  afk_timeout:Int,
  embed_enabled:Boolean,
  embed_channel_id:String,
  verification_level:Int,
  default_message_notifications:Int,
  explicit_content_filter:Int,
  roles:List[Role],
  emojis:List[Guild.Emoji],
  features:List[String],
  mfa_level:Int,
  application_id:Option[String],
  widget_enabled:Boolean,
  widget_channel_id:String,
  joined_at:Option[String],
  large:Option[Boolean],
  member_count:Option[Int],
  voice_states:Option[List[VoiceState]],
  members:Option[List[Guild.Member]],
  channels:Option[List[Channel]]//,
  // presences:Option[List[PresenceUpdate]]
)

object Guild {
  case class Unavailable(id:String)
  case class Embed(enabled:Boolean, channel_id:String)

  case class Member(
    user:User,
    nick:Option[String],
    roles:List[Role],
    joined_at:String,
    deaf:Boolean,
    mute:Boolean
  )

  case class Integration(
    id:String,
    name:String,
    `type`:String,
    enabled:Boolean,
    syncing:Boolean,
    role_id:String,
    expire_behavior:Int,
    expire_grace_period:Int,
    user:User,
    account:IntegrationAccount,
    synced_at:String
  )
  case class IntegrationAccount(id:String, name:String)

  case class Emoji(
    id:String,
    name:String,
    roles:List[String],
    require_colons:Boolean,
    managed:Boolean
  )

  case class Ban(reason:Option[String], user:User)
}