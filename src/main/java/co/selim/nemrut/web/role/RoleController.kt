package co.selim.nemrut.web.role

import co.selim.nemrut.db.Database
import co.selim.nemrut.ext.coroutineHandler
import co.selim.nemrut.ext.json
import co.selim.nemrut.jooq.Tables.ROLE
import co.selim.nemrut.jooq.tables.pojos.Role
import co.selim.nemrut.web.Controller
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

class RoleController(
  vertx: Vertx,
  private val database: Database
) : Controller(vertx) {

  companion object {
    const val BASE_URI = "/roles"
  }

  override fun register(router: Router) {
    handleGet(router)
  }

  private fun handleGet(router: Router) {
    router.get(BASE_URI)
      .coroutineHandler { ctx ->
        val roles = database.withDsl { dsl ->
          dsl.selectFrom(ROLE)
            .fetchInto(Role::class.java)
        }

        ctx.response().json(roles)
      }
  }
}
