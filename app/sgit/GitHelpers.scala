package sgit

import java.io.File

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.{ObjectId, Repository}
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.slf4j.LoggerFactory
import resource._
import util.TempIoHelper

import scala.collection.JavaConverters._

trait GitHelpers {
  private val log = LoggerFactory.getLogger(classOf[GitHelpers])

  def readCommitRange(repo: String, range: CommitRange): List[Commit] = {
    withRepository(repo, git => {
      readGitCommits(git, range)
    })
  }

  def readCommitRange(git: Git, range: CommitRange): List[Commit] = {
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

  def withGit[T](git: Git, whatToDo: Git => T): T = {
    managed(git).acquireAndGet { git =>
      whatToDo(git)
    }
  }

  def withTempRepositoryClone[T](remote: String, whatToDo: Git => T): T = {
    TempIoHelper.withTempDirectory { tempDir =>
      val g = Git.cloneRepository()
        .setDirectory(tempDir)
        .setURI(remote)
        .call()
      withGit(g, { git =>
        whatToDo(git)
      })
    }
  }

  private def readGitCommits(git: Git, commitRange: CommitRange) = {
    log.info(s"Reading commits from branch ${git.getRepository.getBranch} from hash range '$commitRange'")
    (commitRange match {
      case RangeOfCommitsAfter(start, end) => git.log.addRange(ObjectId.fromString(start), ObjectId.fromString(end))
      case AllCommitsAfter(start) => git.log.addRange(ObjectId.fromString(start), git.getRepository.resolve("HEAD"))
      case AllCommits() => git.log().all()
    }).call()
      .asScala
      .toList
      .map(Commit.fromCommit)
      .sortBy(_.commitTime)
  }

  private def createRepository(repo: String): Repository = {
    new FileRepositoryBuilder()
      .setGitDir(new File(repo))
      .readEnvironment() // scan environment GIT_* variables
      .findGitDir() // scan up the file system tree
      .build()
  }
}
