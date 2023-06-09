package co.selim.nemrut.web.salary

import co.selim.nemrut.db.Database
import co.selim.nemrut.ext.coroutineHandler
import co.selim.nemrut.ext.json
import co.selim.nemrut.jooq.Tables.ROLE
import co.selim.nemrut.jooq.Tables.SALARY
import co.selim.nemrut.web.Controller
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import org.jooq.impl.DSL.avg
import java.math.BigDecimal

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

  data class RoleSalary(val title: String, val amount: BigDecimal, val currency: String)

  private fun handleGet(router: Router) {
    router.get(BASE_URI)
      .coroutineHandler { ctx ->
        val salaries = database.withDsl { dsl ->
          dsl.select(ROLE.TITLE, avg(SALARY.AMOUNT), SALARY.CURRENCY)
            .from(SALARY)
            .join(ROLE)
            .on(SALARY.ROLE_ID.eq(ROLE.ID))
            .groupBy(ROLE.TITLE, SALARY.CURRENCY)
            .fetchInto(RoleSalary::class.java)
        }

        ctx.response().json(salaries)
      }
  }
}
