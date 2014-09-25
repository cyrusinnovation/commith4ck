package es

import java.io.File

import io.searchbox.indices.{DeleteIndex, Refresh}
import org.apache.commons.io.FileUtils
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.NodeBuilder
import org.slf4j.LoggerFactory
import org.specs2.mutable.{After, Specification}
import org.specs2.specification.{Fragments, Step}

abstract class EsSpecBase extends Specification with After {
  protected val log = LoggerFactory.getLogger(this.getClass.getName)
  private val esNode = buildEsNode()
  private val jestClient = EsClientProvider.buildJestClient("http://localhost:9200")
  protected val client = new ESClient(jestClient)

  override def map(fs: =>Fragments) = fs ^ Step(destroyEsNode())

  protected def reindex(): Unit = {
    jestClient.execute(new Refresh.Builder().build())
  }

  def after: Unit = {
    jestClient.execute(new DeleteIndex.Builder("_all").build())
  }

  private def buildEsNode() = {
    val nb = new NodeBuilder().local(true).client(false).data(true).settings(ImmutableSettings.builder().put("index.store.type","memory").build())
    val node = nb.node()
    log.info("Started Elasticsearch node")
    node
  }

  private def destroyEsNode(): Unit = {
    jestClient.shutdownClient()
    esNode.close()
    log.info("Stopped Elasticsearch node")
    FileUtils.deleteDirectory(new File("data"))
  }
}
