package es

import io.searchbox.client.JestClient

trait EsSingletonProvider extends EsClientProvider {
  def client(): ESClient = {
    ESClient(EsSingletonProvider.client)
  }
}

object EsSingletonProvider {
  lazy val client: JestClient = EsClientProvider.buildJestClientFromPlayConfiguration()
}