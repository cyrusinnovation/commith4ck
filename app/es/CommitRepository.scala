package es

import io.searchbox.action.Action
import io.searchbox.client.{JestClient, JestResult}
import io.searchbox.core.search.sort.Sort
import io.searchbox.core.{Bulk, Index, Search}
import org.slf4j.Logger
import play.api.libs.json._
import sgit.Commit

trait CommitRepository {
  protected val log: Logger
  def client: JestClient

  def bulkInsertActionForCommits(commits: List[Commit]): Bulk = {
    commits.foldLeft(new Bulk.Builder())((builder, commit) => {
      builder.addAction(indexAction(commit))
    }).build()
  }
  
  def getCommits(page: QueryPage): List[Commit] = {
    val matchAll = """{ query: { "match_all" : { } } }"""
    val query = new Search.Builder(matchAll)
      .addIndex("commits")
      .addType("commit")
      .addSort(new Sort("commitTime", Sort.Sorting.DESC))
      .setParameter("from", page.from)
      .setParameter("size", page.size)
      .build()
    val result = executeAction(query)
    implicit val commitRead = Json.reads[Commit]
    (Json.parse(result.getJsonString) \\ "_source").map(x => Json.fromJson[Commit](x).get).toList
  }

  def indexAction(commit: Commit): Index = {
    new Index.Builder(commit).id(commit.id).index("commits").`type`("commit").refresh(true).build()
  }

  def executeAction[T <: JestResult](action: Action[T]): T = {
    throwIfFail(client.execute(action))
  }

  private def throwIfFail[T <: JestResult](result: T): T = {
    if (!result.isSucceeded) {
      throw new RuntimeException(s"Jest action execution failed. Error: ${result.getErrorMessage}")
    }
    result
  }
}