package controllers.network

import models.commands.InterfaceList
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global

class Link extends Controller {
  def list = Action.async { request =>
    InterfaceList.getList.map(l => Ok(JsArray(l.map(JsString))))
  }

  def ifaceUpdate = Action.async { request =>
    InterfaceList.getList.map(l => Ok(JsArray(l.map(JsString))))
  }
}
