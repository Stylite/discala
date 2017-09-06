package com.tripl3dogdare.discala
package data

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
  def avatarURL = if(avatar.isEmpty) null else Discala.AVI_URL+s"avatars/$id/${avatar.get}.${if(avatar.get.startsWith("a_")) "gif" else "png"}"
  def defaultAvatarURL = Discala.AVI_URL+"embed/avatars/$discriminator.png"
  def displayAvatarURL = if(avatar.isEmpty) defaultAvatarURL else avatarURL
}