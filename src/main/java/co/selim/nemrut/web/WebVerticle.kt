package co.selim.nemrut.web

import co.selim.nemrut.AppConfig
import com.github.michaelbull.logging.InlineLogger
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await

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
      .failureHandler { ctx ->
        if (ctx.response().ended()) return@failureHandler

        LOG.error(ctx.failure()) { "Uncaught exception in router" }
        ctx.response()
          .setStatusCode(500)
          .end()
      }

    controllers.forEach { controller ->
      controller.register(router)
    }

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(appConfig.httpPort)
      .await()
  }
}
