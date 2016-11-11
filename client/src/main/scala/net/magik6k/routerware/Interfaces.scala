package net.magik6k.routerware

import net.magik6k.masj.ajax.REST
import net.magik6k.sjwindow.{WindowManager, Window}
import net.magik6k.tags.Tags.div
import org.scalajs.dom._

import scala.scalajs.js

object Interfaces {
  var window: Window = null
  var creating = false

  def view(): Unit = {
    if(creating) return
    if(window == null) {
      creating = true
      WindowManager.create("interfaces", (250, 200), null, resize = true, w => {
        window = w
        creating = false
        WindowManager.add(window)
        REST.get("/api/v0/link/list", res => {
          val container = window.content.getElementsByClassName("wnd-interfaces-list")(0)
          while(container.firstChild != null) container.removeChild(container.firstChild)

          res.asInstanceOf[js.Array[String]].zipWithIndex.foreach {case (interface, i) =>
            val el = div(interface)
            if(i%2==1) el.withClass("wnd-interfaces-dark")
            container.appendChild(el)
            el.onclick { e: MouseEvent => Interface.open(interface) }
          }
        })
      })
    } else if(!window.isOpen) {
      WindowManager.add(window)
    } else {
      window.focus()
    }
  }

}
