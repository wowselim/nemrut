package co.selim.nemrut

import io.vertx.core.Vertx
import io.vertx.kotlin.core.json.get
import io.vertx.kotlin.coroutines.coAwait

data class AppConfig(
  val httpPort: Int,
  val dbUrl: String,
  val dbUsername: String,
  val dbPassword: String,
) {

  companion object {

    private const val CONFIG_FILENAME = "config.json"
    private const val CONFIG_FS_PATH = "/etc/nemrut/$CONFIG_FILENAME"

    suspend fun load(vertx: Vertx): AppConfig {
      val fsConfigExists = vertx.fileSystem()
        .exists(CONFIG_FS_PATH)
        .coAwait()

      val configPath = if (fsConfigExists) {
        CONFIG_FS_PATH
      } else {
        CONFIG_FILENAME
      }

      val jsonConfig = vertx.fileSystem()
        .readFile(configPath)
        .coAwait()
        .toJsonObject()

      return AppConfig(
        jsonConfig["http.port"],
        jsonConfig["db.url"],
        jsonConfig["db.username"],
        jsonConfig["db.password"],
      )
    }
  }
}
