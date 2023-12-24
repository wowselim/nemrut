package co.selim.nemrut.web.auth

import co.selim.nemrut.web.Controller
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.coAwait

class SignoutController : Controller() {

  companion object {
    const val BASE_URI = "/signout"
  }

  override fun register(router: Router) {
    handlePost(router)
  }

  private fun handlePost(router: Router) {
    router.post(BASE_URI)
      .coroutineHandler { ctx ->
        ctx.session().destroy()
        ctx.end().coAwait()
      }
  }
}
