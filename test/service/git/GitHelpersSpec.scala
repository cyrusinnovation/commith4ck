package service.git

import java.io.File

import org.apache.commons.io
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import resource._
import sgit._
import util.TempIoHelper

@RunWith(classOf[JUnitRunner])
class GitHelpersSpec extends Specification with GitHelpers {
  private val projectGithubUrl = "https://github.com/cyrusinnovation/commith4ck.git"

  "Git helpers" should {
    "should be able to list all commits in a repository" in {
      val commits = TempIoHelper.withTempDirectory { tempDir =>
        val gitDirectory = new File(tempDir, ".git")
        createEmptyRepository(gitDirectory)
        withGit(Git.open(gitDirectory), { git =>
          createTestCommits(Map("a.txt" -> "abc",
            "b.txt" -> "def",
            "c.txt" -> "ghi"), gitDirectory, git)
        })
        readCommitRange(gitDirectory.getAbsolutePath, AllCommits())
      }
      commits.size should beEqualTo(3)
      commits.map(_.id) should beEqualTo(commits.map(_.id))
    }

    "should be able to list all commits after a given commit in a repository" in {
      val commits = TempIoHelper.withTempDirectory { tempDir =>
        val gitDirectory = new File(tempDir, ".git")
        createEmptyRepository(gitDirectory)
        val commits = withGit(Git.open(gitDirectory), { git =>
          createTestCommits(Map("a.txt" -> "abc",
            "b.txt" -> "def",
            "c.txt" -> "ghi"), gitDirectory, git)
        })
        readCommitRange(gitDirectory.getAbsolutePath, AllCommitsAfter(commits.head.id))
      }
      commits.size should beEqualTo(2)
      commits.map(_.id) should beEqualTo(commits.map(_.id))
    }

    "be able to get all of commits after a given hash" in {
      val commits = TempIoHelper.withTempDirectory { tempDir =>
        val gitDirectory = new File(tempDir, ".git")
        createEmptyRepository(gitDirectory)
        val commits = withGit(Git.open(gitDirectory), { git =>
          createTestCommits(Map("a.txt" -> "abc",
            "b.txt" -> "def",
            "c.txt" -> "ghi",
            "d.txt" -> "jkl",
            "e.txt" -> "mno"), gitDirectory, git)
        })
        val allCommits = readCommitRange(gitDirectory.getAbsolutePath, AllCommits())
        val allButFirstCommit = readCommitRange(gitDirectory.getAbsolutePath, RangeOfCommitsAfter(commits(1).id, commits(3).id))
        (allCommits, allButFirstCommit, List(commits(2).id, commits(3).id))
      }

      val allButFirstCommit = commits._2
      val expectedCommitsFromRange = commits._3
      allButFirstCommit.length should beEqualTo(2)
      expectedCommitsFromRange should containTheSameElementsAs(allButFirstCommit.map(_.id))
    }

    "be able to clone a remote repository" in {
      withTempRepositoryClone(projectGithubUrl, git => {
        val clonedProjectRoot = git.getRepository.getDirectory.getParent
        val fileFromClone = new File(clonedProjectRoot, "README.md")
        fileFromClone.exists()
      }) must beTrue
    }
  }

  private def createEmptyRepository(gitDirectory: File) {
    managed(FileRepositoryBuilder.create(gitDirectory)).foreach(_.create())
  }
  private def createTestCommit(filename: String, fileContents: String, repositoryDirectory: File, git: Git): Commit = {
    val targetFile = new File(repositoryDirectory, filename)
    val commitMessage = s"Committing: '${targetFile.getName}' with contents: '$fileContents'"
    io.FileUtils.write(targetFile, fileContents)
    git.add().addFilepattern(targetFile.getName).call()
    Commit(git.commit().setMessage(commitMessage).call())
  }

  private def createTestCommits(fileAndContents: Map[String, String], repositoryDirectory: File, git: Git): List[Commit] = {
    fileAndContents.toList.sortBy(_._1).map(fileAndContent => {
      createTestCommit(fileAndContent._1, fileAndContent._2, repositoryDirectory, git)
    }).toList
  }
}