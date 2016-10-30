package controllers

import play.api.mvc._

class Windows extends Controller {
  def test = Action { Ok(views.html.windows.test()) }
  def interfaces = Action { Ok(views.html.windows.interfaces()) }
}
