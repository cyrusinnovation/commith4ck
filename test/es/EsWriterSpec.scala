package es

import io.searchbox.cluster.Health
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EsWriterSpec extends EsSpecBase {
  "Elasticsearch writer" should {
    "should be able to connect to Elasticsearch" in {
      val result = client.execute(new Health.Builder().build())

      result.isSucceeded must beTrue
    }
  }
}