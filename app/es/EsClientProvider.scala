package es

import io.searchbox.client.config.HttpClientConfig
import io.searchbox.client.{JestClient, JestClientFactory}
import play.api.Play

trait EsClientProvider {
  def client(): ESClient
}

object EsClientProvider {
  def buildJestClient(url: String): JestClient = {
    val jf = new JestClientFactory()
    jf.setHttpClientConfig(new HttpClientConfig.Builder(url).multiThreaded(true).build())
    jf.getObject
  }

  def buildJestClientFromPlayConfiguration(): JestClient = {
    buildJestClient(Play.current.configuration.getString("elasticsearch.url").get)
  }
}



