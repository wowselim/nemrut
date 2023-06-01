package co.selim.nemrut.web

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class Controller(vertx: Vertx) : CoroutineScope {

  override val coroutineContext: CoroutineContext by lazy { vertx.dispatcher() + SupervisorJob() }

  abstract fun register(router: Router)
}
