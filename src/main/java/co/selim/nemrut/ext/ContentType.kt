package co.selim.nemrut.ext

import io.vertx.core.http.HttpHeaders

enum class ContentType(val value: CharSequence) {
  JSON(HttpHeaders.createOptimized("application/json")),
}
