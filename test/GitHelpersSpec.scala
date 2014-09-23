import java.io.File

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import service.git.{CommitRange, GitHelpers}

@RunWith(classOf[JUnitRunner])
class GitHelpersSpec extends Specification with GitHelpers {
  private val projectRepoRoot = new File(new File("."), ".git").getAbsolutePath
  private val projectGithubUrl = "https://github.com/cyrusinnovation/commith4ck.git"
  private val firstCommit = "6d5d0374bfea5ed6f3ce28852222fb3f35b965b9"
  private val fifthProjectCommit = "3a2cfa8f122a4d876b2d6c5b7e107caafe184530"

  "Git helpers" should {
    "be able to get all of commits after a given hash" in {
      val allCommits = readCommitsSince(projectRepoRoot, CommitRange.All)
      val allCommitsAfterFirstCommit = readCommitsSince(projectRepoRoot, CommitRange(firstCommit))

      (allCommits.length - 1) should beEqualTo(allCommitsAfterFirstCommit.length)
    }

    "be able to clone a remote repository" in {
      val allCommitsFromClone = withTempRepositoryClone(projectGithubUrl, git => {
        readCommitsSince(git, CommitRange(firstCommit, fifthProjectCommit))
      })

      val allCommitsFromLocal = readCommitsSince(projectRepoRoot, CommitRange(firstCommit, fifthProjectCommit))
      allCommitsFromLocal.size should equalTo(allCommitsFromClone.size)
    }
  }
}