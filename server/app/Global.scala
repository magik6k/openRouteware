import java.nio.charset.{StandardCharsets, Charset}

import models.Netadmin
import play.api._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
    Netadmin.start()
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
    Netadmin.stop()
  }

}