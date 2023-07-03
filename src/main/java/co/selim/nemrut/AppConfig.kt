package co.selim.nemrut

import com.fasterxml.jackson.annotation.JsonProperty

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
