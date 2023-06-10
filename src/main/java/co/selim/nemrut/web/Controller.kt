package co.selim.nemrut.web

import io.vertx.core.Vertx
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class Controller(vertx: Vertx) : CoroutineScope {

  override val coroutineContext: CoroutineContext by lazy { vertx.dispatcher() + SupervisorJob() }

  abstract fun register(router: Router)

  protected fun Route.coroutineHandler(
    block: suspend (RoutingContext) -> Unit
  ): Route = handler { ctx ->
    launch {
      try {
        block(ctx)
      } catch (t: Throwable) {
        ctx.fail(t)
      }
    }
  }
}
