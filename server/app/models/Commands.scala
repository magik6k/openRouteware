package models

import play.api.libs.json._

object Commands {
  val CMD_PING_REQUEST        = Command("generic.ping.request")
  val CMD_PING_RESPONSE       = Command("generic.ping.response")
  val CMD_ERROR               = Command("generic.error")

  val CMD_GET_INTERFACE_LIST  = Command("link.get")

  val CMD_INTERFACE_LIST      = Command("link.get.response")
}

case class Command(name: String) {
  // Commands.CMD_PING_REQUEST ~ buf
  def ~(to: JsValue): Boolean = {
    name.equals((to \ "cmd").as[String])
  }
}
