package controllers

import play.api.mvc._

object Application extends Controller with CommitsController {
  def index = Action {
    Ok(views.html.main("Commit H4ck"))
  }
}
