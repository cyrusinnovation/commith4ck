package controllers

import es.{EsSingletonProvider, QueryPage}
import org.slf4j.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import sgit.{AllCommits, Commit, GitHelpers}

trait CommitsController extends EsSingletonProvider with GitHelpers { this: Controller with LoggingProvider =>
  implicit val commitRead = Json.writes[Commit]

  def commits = Action {
    try {
      val commits = client().getCommits(QueryPage.Default)
      Ok(Json.toJson(commits))
    } catch {
      case t: Throwable => log.error("Error querying for commits", t); throw t
    }
  }

  def loadCommits(repository: String) = Action {
    val commits = withTempRepositoryClone(repository, readCommitRange(_, AllCommits()))
    val c = client()
    c.executeAction(c.bulkInsertActionForCommits(commits))
    NoContent
  }
}
trait LoggingProvider {
  protected val log: Logger
}