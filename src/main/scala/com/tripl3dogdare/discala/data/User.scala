package com.tripl3dogdare.discala
package data

import io.circe.generic.auto._

case class User(
  id:String,
  username:String,
  discriminator:String,
  avatar:Option[String],
  bot:Option[Boolean],
  mfa_enabled:Option[Boolean],
  verified:Option[Boolean],
  email:Option[String]
) {
  private val avi_endp = "https://cdn.discordapp.com/"
  def avatarURL = if(avatar.isEmpty) null else avi_endp+s"avatars/$id/${avatar.get}.png"
  def defaultAvatarURL = avi_endp+"embed/avatars/$discriminator.png"
  def displayAvatarURL = if(avatar.isEmpty) defaultAvatarURL else avatarURL
}