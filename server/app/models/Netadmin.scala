package models

import java.io.{DataInputStream, BufferedInputStream}
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

import com.google.common.primitives.{Longs, Ints}
import models.commands.InterfaceList
import play.api.libs.json._

object Netadmin {
  var pr: Process = null

  def processCommand(data: Array[Byte]): Unit = {
    val json = Json.parse(data)
    println(s"Rcmd: ${new String(data, StandardCharsets.UTF_8)}")
    val seq = (json \ "seq").as[Long]
    if(Commands.CMD_PING_RESPONSE ~ json)   println("PONG")
    if(Commands.CMD_INTERFACE_LIST ~ json)  InterfaceList.handleResponse(seq, json)
  }

  def start(): Unit = {
    val pb = new ProcessBuilder("server/app/internal/netadmin")
    pb.redirectOutput(ProcessBuilder.Redirect.PIPE)
    pb.redirectInput(ProcessBuilder.Redirect.PIPE)
    pb.redirectError(ProcessBuilder.Redirect.INHERIT)
    pr = pb.start()
    //pr.getOutputStream.write(Array[Byte](0,0,0,12, 0,0,0,0, 0,0,0,0,0,0,0,0))
    //pr.getOutputStream.write(Array[Byte](0,0,0,12, 1,0,0,0, 0,0,0,0,0,0,0,1))
    pr.getOutputStream.flush()

    new Thread("NetAdmin Manager") {
      override def run(): Unit = {
        val stream = new BufferedInputStream(pr.getInputStream)
        val dstream = new DataInputStream(stream)
        var running = true
        while(running) {
          try {
            val toread = dstream.readInt()
            val buf = new Array[Byte](toread)
            val read = dstream.read(buf)
            if (read > 0) {
              processCommand(buf)
            }
          } catch {
            case e: InterruptedException => running = false
          }
        }
      }
    }.start()

  }

  def startCommand(cmd: Command, seq: Long, args: Seq[(String, JsValue)] = Seq.empty): Unit = {
    val json = JsObject(Seq("cmd" -> JsString(cmd.name),
                            "seq" -> JsNumber(seq)) ++ args)
    val bytes = json.toString().getBytes(StandardCharsets.UTF_8)
    pr.getOutputStream.write(Ints.toByteArray(bytes.length))
    pr.getOutputStream.write(bytes)
    pr.getOutputStream.flush()
  }

  def stop(): Unit = {
    pr.destroy()
  }
}
