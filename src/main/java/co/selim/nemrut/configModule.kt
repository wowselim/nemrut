package co.selim.nemrut

import com.fasterxml.jackson.annotation.JsonProperty
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module

data class AppConfig(
  @JsonProperty("http.port")
  val httpPort: Int,
  @JsonProperty("db.url")
  val dbUrl: String,
  @JsonProperty("db.username")
  val dbUsername: String,
  @JsonProperty("db.password")
  val dbPassword: String,
)

val appConfigModule = module {
  single<AppConfig> {
    val vertx = get<Vertx>()
    runBlocking(vertx.dispatcher()) {
      vertx.fileSystem()
        .readFile("config.json")
        .await()
    }
      .toJsonObject()
      .mapTo(AppConfig::class.java)
  }
}
