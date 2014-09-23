package es

import java.io.File

import io.searchbox.client.JestClientFactory
import io.searchbox.client.config.HttpClientConfig
import org.apache.commons.io.FileUtils
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.NodeBuilder
import org.slf4j.LoggerFactory
import org.specs2.mutable.Specification
import org.specs2.specification.{Fragments, Step}

abstract class EsSpecBase extends Specification {
  protected val log = LoggerFactory.getLogger(this.getClass.getName)
  private val esNode = buildEsNode()
  protected val client = buildJestFactory().getObject

  override def map(fs: =>Fragments) = fs ^ Step(destroyEsNode())

  private def buildEsNode() = {
    val nb = new NodeBuilder().local(true).client(false).data(true).settings(ImmutableSettings.builder().put("index.store.type","memory").build())
    val node = nb.node()
    log.info("Started Elasticsearch node")
    node
  }
  private def destroyEsNode(): Unit = {
    client.shutdownClient()
    esNode.close()
    log.info("Stopped Elasticsearch node")
    FileUtils.deleteDirectory(new File("data"))
  }
  private def buildJestFactory() = {
    val jf = new JestClientFactory()
    jf.setHttpClientConfig(new HttpClientConfig.Builder("http://localhost:9200").multiThreaded(true).build())
    jf
  }
}
