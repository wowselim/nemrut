package co.selim.nemrut.web

import co.selim.nemrut.AppConfig
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import org.slf4j.LoggerFactory

class WebVerticle(
  private val appConfig: AppConfig,
  private val controllers: List<Controller>,
) : CoroutineVerticle() {

  companion object {
    private val LOG = LoggerFactory.getLogger(WebVerticle::class.java)
  }

  override suspend fun start() {
    val router = Router.router(vertx)

    router.route()
      .failureHandler { ctx ->
        if (ctx.response().ended()) return@failureHandler

        LOG.error("Uncaught exception in router", ctx.failure())
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
