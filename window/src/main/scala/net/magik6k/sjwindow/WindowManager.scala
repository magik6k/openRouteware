package net.magik6k.sjwindow

import net.magik6k.masj.ajax.ResourceManager
import net.magik6k.tags.Tag._
import org.scalajs.dom
import dom.document
import dom.window
import org.scalajs.dom.raw.{HTMLElement, Attr}

object WindowManager {
  private var lastZIndex = 0
  def add(window: Window): Unit = {
    if(window.isOpen) return
    document.getElementById("window-container").appendChild(window.content)
    window.open = true
    window.focus()
  }
  def create(url: String, windowSize: (Int, Int), wPos: (Int, Int), resize: Boolean, done: ((Window) => Any)) {
    var windowPos = wPos
    ResourceManager.get("/windows/" + url, data => {
      val element = document.createElement("div")
      element.innerHTML = if (data != null) data else "ERROR, cannot get window data"
      element.getElementsByClassName("window-header")(0).asInstanceOf[HTMLElement].setAttribute("draggable", "true")

      if(windowPos == null) {
        windowPos = (window.innerWidth / 2 - windowSize._1 / 2, window.innerHeight / 2 - windowSize._2 / 2)
      }

      done(new Window(windowSize, windowPos, element, resize))
    })
  }
  private[sjwindow] def getNextZIndex = {
    lastZIndex += 1
    lastZIndex
  }
}
