package controllers

import es.{EsSingletonProvider, QueryPage}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import sgit.{AllCommits, Commit, GitHelpers}
import util.LoggingProvider

trait CommitsController extends EsSingletonProvider with GitHelpers { this: Controller with LoggingProvider =>
  implicit val commitRead = Json.writes[Commit]

  def commits = Action {
      val commits = client().getCommits(QueryPage.Default)
      Ok(Json.toJson(commits))
  }

  def loadCommits(repository: String) = Action {
    val commits = withTempRepositoryClone(repository, readCommitRange(_, AllCommits()))
    val c = client()
    c.executeAction(c.bulkInsertActionForCommits(commits))
    NoContent
  }
}
