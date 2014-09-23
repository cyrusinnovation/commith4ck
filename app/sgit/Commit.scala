package sgit

import org.eclipse.jgit.revwalk.RevCommit

case class Commit(id: String, commitTime: Int)

object Commit {
  def apply(rev: RevCommit): Commit = {
    Commit(rev.getId.name(), rev.getCommitTime)
  }
}
