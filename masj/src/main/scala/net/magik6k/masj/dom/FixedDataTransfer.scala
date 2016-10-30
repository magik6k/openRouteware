package net.magik6k.masj.dom

import org.scalajs.dom.Element
import org.scalajs.dom.raw.DragEvent

import scala.scalajs.js

@js.native
trait FixedDataTransfer extends DragEvent {
  def setDragImage(img: Element, xOffset: Double, yOffset: Double): js.Any = js.native
}
