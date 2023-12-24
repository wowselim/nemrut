package co.selim.nemrut.web.auth

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.HttpException

class AuthzHandler(
  private val authzProvider: AuthzProvider,
  private vararg val permissions: Permission,
) : Handler<RoutingContext> {

  override fun handle(ctx: RoutingContext) {
    if (ctx.user() == null) {
      throw HttpException(401)
    }

    authzProvider.getAuthorizations(ctx.user())
      .onComplete {
        val isAuthorized = permissions.any { permission ->
          permission.authorization.match(ctx.user())
        }
        if (isAuthorized) {
          ctx.next()
        } else {
          ctx.fail(HttpException(403))
        }
      }
  }
}
