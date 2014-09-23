package service.git

import java.io.File

import SGit.{CommitRange, GitHelpers}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GitHelpersSpec extends Specification with GitHelpers {
  private val projectRepoRoot = new File(new File("."), ".git").getAbsolutePath
  private val projectGithubUrl = "https://github.com/cyrusinnovation/commith4ck.git"
  private val firstCommit = "6d5d0374bfea5ed6f3ce28852222fb3f35b965b9"
  private val fifthProjectCommit = "3a2cfa8f122a4d876b2d6c5b7e107caafe184530"

  "Git helpers" should {
    "be able to get all of commits after a given hash" in {
      val allCommits = readCommitRange(projectRepoRoot, CommitRange())
      val allCommitsAfterFirstCommit = readCommitRange(projectRepoRoot, CommitRange(firstCommit))

      (allCommits.length - 1) should beEqualTo(allCommitsAfterFirstCommit.length)
    }

    "be able to clone a remote repository" in {
      val fromFirstToFifthCommit = CommitRange(firstCommit, fifthProjectCommit)
      val allCommitsFromClone = withTempRepositoryClone(projectGithubUrl, git => {
        readCommitRange(git, fromFirstToFifthCommit)
      })
      val allCommitsFromLocal = readCommitRange(projectRepoRoot, fromFirstToFifthCommit)

      allCommitsFromLocal.size should beEqualTo(allCommitsFromClone.size)
    }
  }
}