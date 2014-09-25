package es

import io.searchbox.client.config.HttpClientConfig
import io.searchbox.client.{JestClient, JestClientFactory}
import org.slf4j.LoggerFactory
import play.api.Play

trait EsClientProvider {
  def client(): ESClient
}

object EsClientProvider {
  private val log = LoggerFactory.getLogger(classOf[EsClientProvider])

  def buildJestClient(url: String): JestClient = {
    val jf = new JestClientFactory()
    jf.setHttpClientConfig(new HttpClientConfig.Builder(url).multiThreaded(true).build())
    val client = jf.getObject
    log.info(s"Jest client created for url: $url")
    client
  }

  def buildJestClientFromPlayConfiguration(): JestClient = {
    buildJestClient(Play.current.configuration.getString("elasticsearch.url").get)
  }
}



