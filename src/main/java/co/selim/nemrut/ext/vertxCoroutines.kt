package co.selim.nemrut.ext

import io.vertx.core.Vertx
import io.vertx.core.impl.ContextInternal
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch

suspend fun <T> awaitBlockingSuspend(block: suspend () -> T): T {
  return awaitResult { handler ->
    val ctx = Vertx.currentContext()
    ctx.executeBlocking<T>({ promise ->
      val workerDispatcher = (ctx as ContextInternal).workerPool().executor().asCoroutineDispatcher()
      CoroutineScope(workerDispatcher).launch {
        val prev = ctx.beginDispatch()
        try {
          promise.complete(block())
        } catch (t: Throwable) {
          promise.fail(t)
        } finally {
          ctx.endDispatch(prev)
        }
      }
    }, { ar ->
      handler.handle(ar)
    })
  }
}
