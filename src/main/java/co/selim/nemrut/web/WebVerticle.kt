package co.selim.nemrut.web

import co.selim.nemrut.AppConfig
import com.github.michaelbull.logging.InlineLogger
import io.reactiverse.contextual.logging.ContextualData
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.ext.web.handler.PlatformHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.sstore.SessionStore
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait
import java.util.*

class WebVerticle(
  private val appConfig: AppConfig,
  private val controllers: Set<Controller>,
) : CoroutineVerticle() {

  companion object {
    private val LOG = InlineLogger()
  }

  override suspend fun start() {
    val router = Router.router(vertx)

    router.route()
      .handler(PlatformHandler { ctx ->
        ContextualData.put("requestId", UUID.randomUUID().toString())
        ctx.next()
      })
      .handler(LoggerHandler.create())
      .handler(SessionHandler.create(SessionStore.create(vertx)))
      .failureHandler(FailureHandler)

    controllers.forEach { controller ->
      controller.register(router)
    }

    val server = vertx.createHttpServer()
      .requestHandler(router)
      .listen(appConfig.httpPort)
      .coAwait()

    LOG.info { "Listening on port ${server.actualPort()}" }
  }
}
