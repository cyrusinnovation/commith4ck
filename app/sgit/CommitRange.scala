package sgit

sealed abstract class CommitRange

sealed case class AllCommits() extends CommitRange
sealed case class RangeOfCommitsAfter(start: String, end: String) extends CommitRange
sealed case class AllCommitsAfter(start: String) extends CommitRange

object CommitRange {
  def apply(start: String): CommitRange = AllCommitsAfter(start)
  def apply(start: String, end: String): CommitRange = RangeOfCommitsAfter(start, end)
  def apply() = AllCommits()
}