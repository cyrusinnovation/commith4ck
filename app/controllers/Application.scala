package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    //comment to check assumption
    Ok(views.html.index("Your new application is ready. YAY."))
  }

}