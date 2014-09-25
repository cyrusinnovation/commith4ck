package controllers

import org.slf4j.LoggerFactory
import play.api.mvc._
import util.LoggingProvider

object Application extends Controller with CommitsController with LoggingProvider {
  protected val log = LoggerFactory.getLogger("application")

  def index = Action {
    Ok(views.html.main("Commit H4ck"))
  }
}
