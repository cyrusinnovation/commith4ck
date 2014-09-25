package es

import io.searchbox.client.JestClient
import org.slf4j.LoggerFactory

case class ESClient(client: JestClient) extends CommitWriter {
  protected val log = LoggerFactory.getLogger(classOf[ESClient])
}
