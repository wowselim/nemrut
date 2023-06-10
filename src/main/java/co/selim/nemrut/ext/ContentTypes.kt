package co.selim.nemrut.ext

import io.vertx.core.http.HttpHeaders

object ContentTypes {
  val JSON: CharSequence = HttpHeaders.createOptimized("application/json")
}
