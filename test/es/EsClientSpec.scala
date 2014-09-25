package es

import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import sgit.Commit

@RunWith(classOf[JUnitRunner])
class EsClientSpec extends EsSpecBase {
  private val hasher = Hashing.sha1()
  def hash(str: String) = hasher.newHasher().putString(str, Charsets.UTF_8).hash().toString

  "Elasticsearch client" should {
    "should be able to write commits" in {
      val commits = List(new Commit(hash("Commit 1"), 1), new Commit(hash("Commit 2"), 2))
      val result = client.executeAction(client.bulkInsertActionForCommits(commits))

      result.isSucceeded must beTrue
    }

    "should be able to read commits" in {
      val commits = Range(1,11).map(n => new Commit(hash(s"Commit $n"), n)).toList
      log.info(commits.toString())
      client.executeAction(client.bulkInsertActionForCommits(commits))

      reindex()
      val firstPage = QueryPage(0,5)
      val commitsFirstPage = client.getCommits(firstPage)
      val commitsSecondPage = client.getCommits(firstPage.next())
      log.info(commitsFirstPage.toString())
      log.info(commitsSecondPage.toString())
      (commitsFirstPage.size + commitsSecondPage.size) must be_==(commits.size)
      (commitsFirstPage ::: commitsSecondPage) must containTheSameElementsAs(commits)
    }
  }
}
