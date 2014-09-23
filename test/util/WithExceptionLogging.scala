package util

import org.slf4j.LoggerFactory
import org.specs2.execute.{Result, AsResult}
import org.specs2.specification.Around

trait WithExceptionLogging extends Around {
  import util.WithExceptionLogging.log

  def around[T: AsResult](t: => T): Result = {
    try {
      AsResult(t)
    } catch {
      case t: Throwable =>
        log.error("Error thrown during test", t)
        throw t
    }
  }
}

object WithExceptionLogging {
  private val log = LoggerFactory.getLogger(classOf[WithExceptionLogging])
}
