package co.selim.nemrut.web

import com.github.michaelbull.logging.InlineLogger
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.HttpException

object FailureHandler : Handler<RoutingContext> {

  private val LOG = InlineLogger()

  override fun handle(ctx: RoutingContext) {
    if (ctx.response().ended()) return

    when (val failure = ctx.failure()) {
      is HttpException -> {
        ctx.response()
          .setStatusCode(failure.statusCode)
          .end(failure.localizedMessage)
      }

      else -> {
        LOG.error(ctx.failure()) { "Uncaught exception in router" }
        ctx.response()
          .setStatusCode(500)
          .end()
      }
    }
  }
}
