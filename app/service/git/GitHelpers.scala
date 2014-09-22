package service.git

import java.io.File

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.{ObjectId, Repository}
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.slf4j.LoggerFactory
import resource._

import scala.collection.JavaConverters._

trait GitHelpers {
  private val log = LoggerFactory.getLogger(classOf[GitHelpers])

  def readCommitsSince(repo: String, fromCommit: Option[String], untilCommit: Option[String]) = {
    withRepository(repo, git => {
      readGitCommits(git, fromCommit, untilCommit)
    })
  }

  def readCommitsSince(git: Git, fromCommit: Option[String], untilCommit: Option[String]) = {
    withGit(git, git => {
      readGitCommits(git, fromCommit, untilCommit)
    })
  }

  def withRepository[T](repo: String, whatToDo: Git => T): T = {
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
    val tempDir = File.createTempFile("commith4ck", "tmp")
    tempDir.delete()
    tempDir.mkdir()
    val g = Git.cloneRepository()
      .setDirectory(tempDir)
      .setURI(remote)
      .call()
    withGit(g, { git =>
      whatToDo(git)
    })
  }

  private def readGitCommits(git: Git, fromCommit: Option[String], untilCommit: Option[String]): List[RevCommit] = {
    val currentBranch = git.getRepository.getBranch
    log.info(s"Reading commits from branch $currentBranch from hash range '${fromCommit.getOrElse('-')}' -> '${untilCommit.getOrElse('-')}'")

    fromCommit
      .fold(git.log().all())(x => git.log.addRange(ObjectId.fromString(x), untilCommit.map(ObjectId.fromString).getOrElse(git.getRepository.resolve("HEAD"))))
      .call()
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
}
