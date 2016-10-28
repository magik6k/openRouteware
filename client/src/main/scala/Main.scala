import org.example.HelloLib
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js.JSApp

import org.scalajs.dom.document

object Main extends JSApp {
  def main(): Unit = {
    new HelloLib(document.getElementsByClassName("test")(0).asInstanceOf[HTMLElement])
  }
}
