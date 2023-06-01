package co.selim.nemrut.ext

import co.selim.nemrut.ext.ContentType.JSON
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.await
import java.net.URI
import java.util.*

suspend fun HttpServerResponse.created(body: Any, location: URI) {
  setStatusCode(201)
    .putHeader(HttpHeaders.LOCATION, location.toASCIIString())
    .json(body)
}

suspend fun HttpServerResponse.notFound() {
  setStatusCode(404)
    .end()
    .await()
}

suspend fun HttpServerResponse.json(body: Any) {
  putHeader(HttpHeaders.CONTENT_TYPE, JSON.value)
    .end(Json.encode(body))
    .await()
}

suspend fun HttpServerResponse.jsonOrNotFound(body: Any?) {
  if (body == null) {
    notFound()
  } else {
    json(body)
  }
}

inline fun <reified T> RoutingContext.requireBody(): T {
  return checkNotNull(body().asPojo(T::class.java))
}

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.requireId(param: String): UUID {
  return UUID.fromString(pathParam(param))
}
