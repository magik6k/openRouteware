package models.commands

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

import models.{Commands, Netadmin, SeqProvider}

import scala.collection.mutable
import scala.concurrent._

object InterfaceList {
  val todo = mutable.Map[Long, Promise[Seq[String]]]()

  def handleResponse(seq: Long, data: Array[Byte]): Unit = {
    val interfaces = ByteBuffer.wrap(data, 12, 4).getInt()

    if(todo.contains(seq)) {
      todo(seq).success((1 to interfaces).map(i => new String(data, i * 16, 16, StandardCharsets.UTF_8).trim))
      todo -= seq
    } else {
      println("W: Got interface list with unknown seq")
    }
  }

  def getList: Future[Seq[String]] = {
    val p = Promise[Seq[String]]()
    val seq = SeqProvider.provide()
    Netadmin.startCommand(Commands.CMD_GET_INTERFACE_LIST, seq)
    todo += seq -> p
    p.future
  }
}
