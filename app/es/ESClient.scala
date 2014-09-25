package es

import io.searchbox.client.JestClient
import org.slf4j.LoggerFactory

case class ESClient(client: JestClient) extends CommitRepository {
  protected val log = LoggerFactory.getLogger(classOf[ESClient])
}
