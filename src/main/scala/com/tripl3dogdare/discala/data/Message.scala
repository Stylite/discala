package com.tripl3dogdare.discala
package data

case class Message(
  id:String,
  channel_id:String,
  author:User,
  content:String,
  timestamp:String,
  edited_timestamp:String,
  tts:Boolean,
  mention_everyone:Boolean,
  mentions:List[User],
  mention_roles:List[String],
  attachments:List[Message.Attachment],
  embeds:List[Message.Embed],
  reactions:Option[List[Message.Reaction]],
  nonce:Option[String],
  pinned:Boolean,
  webhook_id:Option[String],
  `type`:Int
) {
  def isWebhook = !webhook_id.isEmpty
  def isSystem =`type`!= 0
}

object Message {
  case class Attachment(
    id:String,
    filename:String,
    size:Int,
    url:String,
    proxy_url:String,
    height:Option[Int],
    width:Option[Int]
  )

  case class Embed(
    title:String,
    `type`:String,
    description:String,
    url:String,
    timestamp:String,
    color:Int,
    image:Embed.Media,
    thumbnail:Embed.Media,
    video:Embed.Media,
    provider:Embed.Provider,
    author:Embed.Author,
    footer:Embed.Footer,
    fields:List[Embed.Field]
  )

  object Embed {
    case class Media(url:String, proxy_url:Option[String], height:Int, width:Int)
    case class Provider(name:String, url:String)
    case class Author(name:String, url:String, icon_url:String, proxy_icon_url:String)
    case class Footer(text:String, icon_url:String, proxy_icon_url:String)
    case class Field(name:String, value:String, inline:Boolean)
  }

  case class Reaction(
    count:Int,
    me:Boolean,
    emoji:Guild.Emoji
  )
}