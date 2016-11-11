package models

object Commands {
  val CMD_PING_REQUEST        = Command(0,0,0,0)
  val CMD_PING_RESPONSE       = Command(0,0,0,1)
  val CMD_ERROR               = Command(0,0,0,2)

  val CMD_GET_INTERFACE_LIST  = Command(1,0,0,0)

  val CMD_INTERFACE_LIST      = Command(2,0,0,0)
}

case class Command(bytes: Byte*) {
  // Commands.CMD_PING_REQUEST ~ buf
  def ~(to: Array[Byte]): Boolean = {
    bytes.zipWithIndex.forall{case (b: Byte, idx: Int) => b == to(idx)}
  }

  def +(data: Array[Byte]) = {
    (bytes ++ data).toArray
  }
}
