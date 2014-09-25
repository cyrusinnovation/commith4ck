import java.security.KeyStore
import javax.net.ssl.{TrustManagerFactory, X509TrustManager}

import org.slf4j.LoggerFactory
import play.api._
import play.api.mvc._
import play.filters.gzip.GzipFilter

object Global extends WithFilters(new GzipFilter()) with GlobalSettings {
  private val log = LoggerFactory.getLogger("global")
  // onStart, onStop etc...
  override def beforeStart(app: Application): Unit = {
    super.beforeStart(app)
    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)

    trustManagerFactory.init(null.asInstanceOf[KeyStore])

    log.info("JVM Default Trust Managers:")
    trustManagerFactory.getTrustManagers.foreach(trustManager => {
      log.info(trustManager.toString)
      trustManager match {
        case x509TrustManager: X509TrustManager =>
          log.info("\tAccepted issuers count : " + x509TrustManager.getAcceptedIssuers.length)
        case _ =>
      }
    })

  }
}