package net.magik6k.sjwindow

import net.magik6k.masj.dom.{RemovableElement, FixedDataTransfer}
import org.scalajs.dom.raw.{HTMLElement, Element}
import org.scalajs.dom
import org.scalajs.dom.window
import net.magik6k.tags.Tags._
import net.magik6k.tags.Tag._

class Window(private var windowSize: (Int, Int), private var windowPos: (Int, Int), val content: Element, resize: Boolean = true) {
  val (width: Int, height: Int) = windowSize
  var (x: Int, y: Int) = windowPos
  private[sjwindow] var open: Boolean = false
  var dragX: Int = 0
  var dragY: Int = 0
  content.asInstanceOf[HTMLElement].ondragstart = {
    e: dom.DragEvent => {
      val img = div().css("display" -> "none")
      e.dataTransfer.asInstanceOf[FixedDataTransfer].setDragImage(img, 0, 0)
      e.dataTransfer.effectAllowed = "copy"
      e.dataTransfer.setData("text/plain", "window")
      dragX = e.screenX.toInt - x
      dragY = e.screenY.toInt - y
    }
  }
  content.asInstanceOf[HTMLElement].ondrag = {
    e: dom.DragEvent => {
      x = e.screenX.toInt - dragX
      y = e.screenY.toInt - dragY
      updatePosition()
    }
  }
  content.asInstanceOf[HTMLElement].ondragend = {
    e: dom.DragEvent => {
      x = e.screenX.toInt - dragX
      y = e.screenY.toInt - dragY
      updatePosition()
    }
  }

  content.asInstanceOf[HTMLElement].onmousedown = {
    e: dom.MouseEvent => {
      focus()
    }
  }

  content.getElementsByClassName("window-header-cross")(0).asInstanceOf[HTMLElement].onclick = { e: dom.MouseEvent =>
    close()
  }

  updatePosition()
  updateSize()

  def updateSize() {
    val e = content.getElementsByClassName("window-root")(0).asInstanceOf[HTMLElement]
    e.style.setProperty("width", width.toString + "px")
    e.style.setProperty("height", height.toString + "px")
    e.style.setProperty("resize", if (resize) "both" else "none")
  }

  def focus() {
    content.asInstanceOf[HTMLElement].style.setProperty("z-index", (WindowManager.getNextZIndex * 10).toString)
  }

  def updatePosition() {
    val e = content.asInstanceOf[HTMLElement]
    e.style.setProperty("top", y.toString + "px")
    e.style.setProperty("left", x.toString + "px")
  }

  def close() {
    content.asInstanceOf[RemovableElement].remove()
    open = false
  }
  def isOpen = open
}
