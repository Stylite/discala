package com.tripl3dogdare.discala
package data

case class VoiceState(
  guild_id:Option[String],
  channel_id:String,
  user_id:String,
  session_id:String,
  deaf:Boolean,
  mute:Boolean,
  self_deaf:Boolean,
  self_mute:Boolean,
  suppress:Boolean
)

case class VoiceRegion(
  id:String,
  name:String,
  sample_hostname:String,
  sample_port:String,
  vip:Boolean,
  optimal:Boolean,
  deprecated:Boolean,
  custom:Boolean
)