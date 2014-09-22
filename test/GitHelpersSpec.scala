import java.io.File

import org.apache.commons.io.FileUtils
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import service.git.GitHelpers

@RunWith(classOf[JUnitRunner])
class GitHelpersSpec extends Specification with GitHelpers {
  private val log = LoggerFactory.getLogger(classOf[GitHelpersSpec])
  private val projectRepoRoot = new File(new File("."), ".git").getAbsolutePath
  private val projectGithubUrl = "https://github.com/cyrusinnovation/commith4ck.git"
  private val projectGithubGitUrl = "git@github.com:cyrusinnovation/commith4ck.git"
  private val firstCommit = "6d5d0374bfea5ed6f3ce28852222fb3f35b965b9"
  private val fifthProjectCommit = "3a2cfa8f122a4d876b2d6c5b7e107caafe184530"

  "Git helpers" should {
    "be able to get all of commits after a given hash" in {
      val allCommits = readCommitsSince(projectRepoRoot, None, None)
      val allCommitsAfterFirstCommit = readCommitsSince(projectRepoRoot, Some(firstCommit), None)

      (allCommits.length - 1) should beEqualTo(allCommitsAfterFirstCommit.length)
    }

    "be able to clone a remote repository" in new TestCloneCleaner {
      val testCloneRoot = new File(".", "GitCloneTestDir")

      val allCommitsFromClone = withTempRepositoryClone(projectGithubUrl, git => {
        readCommitsSince(git, Some(firstCommit), Some(fifthProjectCommit))
      })

      val allCommitsFromLocal = readCommitsSince(projectRepoRoot, Some(firstCommit), Some(fifthProjectCommit))


      allCommitsFromLocal.size should equalTo(allCommitsFromClone.size)
      true
    }
  }
}

trait TestCloneCleaner extends After {
  val testCloneRoot: File

  def after = {
    FileUtils.deleteDirectory(testCloneRoot)
  }
}