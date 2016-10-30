package net.magik6k.routerware

import com.sun.glass.events.MouseEvent
import net.magik6k.sjwindow.WindowManager
import net.magik6k.tags.Tags.div
import org.scalajs.dom.document
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    document.getElementsByClassName("focus-interfaces")(0).asInstanceOf[HTMLElement].onclick = {
      e: dom.MouseEvent =>
        Interfaces.view()
    }
  }
}
