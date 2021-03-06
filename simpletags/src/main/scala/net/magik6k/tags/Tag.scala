package net.magik6k.tags

import org.scalajs.dom._
import org.scalajs.dom.raw.HTMLElement

abstract class Tag(inner: Tag*) {
  def getNode: Node

  def hide() = {
    getNode.asInstanceOf[HTMLElement].style.display = "none"
    this
  }

  def show() = {
    getNode.asInstanceOf[HTMLElement].style.display = ""
    this
  }
}

object Tag {
  implicit def ofString(str: String): Tag = {
    new Tag() {
      val element = document.createElement("span")
      element.innerHTML = str.replaceAll(">","&gt;").replaceAll("<","&lt;")
      override def getNode: Node = element
    }
  }

  implicit def ofElement(element: Element): Tag = {
    new Tag() {
      override def getNode: Node = element
    }
  }

  implicit def elementTag(tag: Tag): Element = tag.getNode.asInstanceOf[Element]

  /**
    * Wraps DOM tag into Tag
    *
    * @param element Element to wrap
    * @return Resulting tag
    */
  def apply(element: Element) = ofElement(element)

  /**
    * Creates tag from html code
    *
    * @param html Code string
    * @return html code inside div block
    */
  def apply(html: String, el: Element = document.createElement("div")): Tag = {
    new Tag() {
      val element = el
      element.innerHTML = html

      override def getNode: Node = element
    }
  }
}
