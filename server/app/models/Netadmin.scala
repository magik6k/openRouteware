package models

import java.io.{DataInputStream, BufferedInputStream}
import java.nio.ByteBuffer

import com.google.common.primitives.{Longs, Ints}
import models.commands.InterfaceList

object Netadmin {
  var pr: Process = null

  def processCommand(data: Array[Byte]): Unit = {
    val seq = ByteBuffer.wrap(data, 4, 8).getLong
    println(s"SSEQ: $seq")
    if(Commands.CMD_PING_RESPONSE ~ data)   println("PONG")
    if(Commands.CMD_INTERFACE_LIST ~ data)  InterfaceList.handleResponse(seq, data)
  }

  def start(): Unit = {
    val pb = new ProcessBuilder("server/app/internal/netadmin")
    pb.redirectOutput(ProcessBuilder.Redirect.PIPE)
    pb.redirectInput(ProcessBuilder.Redirect.PIPE)
    pb.redirectError(ProcessBuilder.Redirect.INHERIT)
    pr = pb.start()
    pr.getOutputStream.write(Array[Byte](0,0,0,12, 0,0,0,0, 0,0,0,0,0,0,0,0))
    pr.getOutputStream.write(Array[Byte](0,0,0,12, 1,0,0,0, 0,0,0,0,0,0,0,1))
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

  def startCommand(cmd: Command, seq: Long, args: Seq[Byte] = Seq.empty): Unit = {
    pr.getOutputStream.write(Ints.toByteArray(4 + 8 + args.length))
    pr.getOutputStream.write(cmd.bytes.toArray)
    pr.getOutputStream.write(Longs.toByteArray(seq))
    pr.getOutputStream.write(args.toArray)
    pr.getOutputStream.flush()
  }

  def stop(): Unit = {
    pr.destroy()
  }
}
