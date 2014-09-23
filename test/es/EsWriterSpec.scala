package es

import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import sgit.Commit

@RunWith(classOf[JUnitRunner])
class EsWriterSpec extends EsSpecBase with CommitWriter {
  val hasher = Hashing.sha1()

  def hash(str: String) = hasher.newHasher().putString(str, Charsets.UTF_8).hash().toString

  "Elasticsearch writer" should {
    "should be able to connect to Elasticsearch" in {
      val commits = List(new Commit(hash("Commit 1"), 1), new Commit(hash("Commit 2"), 2))
      val result = executeAction(client, bulkInsertActionForCommits(commits))
      result.isSucceeded must beTrue
    }
  }
}
