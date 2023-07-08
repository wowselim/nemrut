package co.selim.nemrut

import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.get

data class AppConfig(
  val httpPort: Int,
  val dbUrl: String,
  val dbUsername: String,
  val dbPassword: String,
) {

  companion object {

    fun fromBuffer(buffer: Buffer): AppConfig {
      val jsonObject = JsonObject(buffer)
      return AppConfig(
        jsonObject["http.port"],
        jsonObject["db.url"],
        jsonObject["db.username"],
        jsonObject["db.password"],
      )
    }
  }
}
