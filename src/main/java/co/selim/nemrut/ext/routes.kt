package co.selim.nemrut.ext

import io.vertx.ext.web.Route
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

context(CoroutineScope)
fun Route.coroutineHandler(
  block: suspend (RoutingContext) -> Unit
): Route = handler { ctx ->
  launch(ctx.vertx().dispatcher()) {
    try {
      block(ctx)
    } catch (t: Throwable) {
      ctx.fail(t)
    }
  }
}
