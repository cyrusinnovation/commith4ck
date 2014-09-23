package es

import io.searchbox.action.Action
import io.searchbox.client.{JestClient, JestResult}
import io.searchbox.core.{Index, Bulk}
import sgit.Commit

trait CommitWriter {
  def bulkInsertActionForCommits(commits: List[Commit]): Bulk = {
    commits.foldLeft(new Bulk.Builder())((builder, commit) => {
      builder.addAction(indexAction(commit))
    }).build()
  }

  def indexAction(commit: Commit): Index = {
    new Index.Builder(commit).id(commit.id).index("commits").`type`("commit").refresh(true).build()
  }

  def executeAction[T <: JestResult](client: JestClient, action: Action[T]): T = {
    throwIfFail(client.execute(action))
  }

  private def throwIfFail[T <: JestResult](result: T): T = {
    if (!result.isSucceeded) {
      throw new RuntimeException("Jest action execution failed")
    }
    result
  }
}
