package service.git

import java.io.File

import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.{ObjectId, Repository}
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.slf4j.LoggerFactory
import resource._

import scala.collection.JavaConverters._

trait GitHelpers {
  private val log = LoggerFactory.getLogger(classOf[GitHelpers])

  def readCommitsSince(repo: String, range: CommitRange) = {
    withRepository(repo, git => {
      readGitCommits(git, range)
    })
  }

  def readCommitsSince(git: Git, range: CommitRange) = {
    withGit(git, git => {
      readGitCommits(git, range)
    })
  }

  private def withRepository[T](repo: String, whatToDo: Git => T): T = {
    managed(createRepository(repo)).acquireAndGet { repo =>
      withGit(new Git(repo), git => {
        whatToDo(git)
      })
    }
  }

  private def withGit[T](git: Git, whatToDo: Git => T): T = {
    managed(git).acquireAndGet { git =>
      whatToDo(git)
    }
  }

  def withTempRepositoryClone[T](remote: String, whatToDo: Git => T): T = {
    val tempDir = createTempDirectory()
    try {
      val g = Git.cloneRepository()
        .setDirectory(tempDir)
        .setURI(remote)
        .call()
      withGit(g, { git =>
        whatToDo(git)
      })
    } finally {
      FileUtils.deleteDirectory(tempDir)
    }
  }

  private def readGitCommits(git: Git, commitRange: CommitRange): List[RevCommit] = {
    log.info(s"Reading commits from branch ${git.getRepository.getBranch} from hash range '$commitRange'")

    (commitRange match {
      case CommitRange.All => git.log().all()
      case CommitRange(start, None) => git.log.addRange(ObjectId.fromString(start), git.getRepository.resolve("HEAD"))
      case CommitRange(start, Some(end)) => git.log.addRange(ObjectId.fromString(start), ObjectId.fromString(end))
    }).call()
      .asScala
      .toList
      .sortBy(_.getCommitTime)
  }

  private def createRepository(repo: String): Repository = {
    new FileRepositoryBuilder()
      .setGitDir(new File(repo))
      .readEnvironment() // scan environment GIT_* variables
      .findGitDir() // scan up the file system tree
      .build()
  }

  private def createTempDirectory() = {
    val tempDir = File.createTempFile("commith4ck", "tmp")
    tempDir.delete()
    tempDir.mkdir()
    tempDir
  }

}

case class CommitRange(start: String, end: Option[String])
object CommitRange {
  def apply(start: String): CommitRange = CommitRange(start, None)
  def apply(start: String, end: String): CommitRange = CommitRange(start, Option(end))
  val All = new CommitRange(null, None)
}