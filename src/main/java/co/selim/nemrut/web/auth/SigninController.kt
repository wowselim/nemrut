package co.selim.nemrut.web.auth

import co.selim.nemrut.web.Controller
import co.selim.nemrut.web.ext.json
import co.selim.nemrut.web.ext.requireBody
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.coAwait

class SigninController(
  private val authnProvider: AuthnProvider,
) : Controller() {

  companion object {
    const val BASE_URI = "/signin"
  }

  override fun register(router: Router) {
    handlePost(router)
  }

  data class LoginDto(val username: String, val password: String)

  private fun handlePost(router: Router) {
    router.post(BASE_URI)
      .handler(BodyHandler.create(false))
      .coroutineHandler { ctx ->
        val body = ctx.requireBody<LoginDto>()
        val credentials = UsernamePasswordCredentials(body.username, body.password)
        val user = authnProvider.authenticate(credentials).coAwait()
        ctx.setUser(user)
        ctx.response().json(JsonObject().put("username", body.username))
      }
  }
}
