package co.selim.nemrut.web.salary

import co.selim.nemrut.db.Database
import co.selim.nemrut.ext.coroutineHandler
import co.selim.nemrut.ext.jsonOrNotFound
import co.selim.nemrut.ext.requireId
import co.selim.nemrut.jooq.Tables.*
import co.selim.nemrut.web.Controller
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import org.jooq.impl.DSL.avg
import java.math.BigDecimal

class SalaryController(
  vertx: Vertx,
  private val database: Database
) : Controller(vertx) {

  companion object {
    const val BASE_URI = "/salaries"
  }

  override fun register(router: Router) {
    handleGet(router)
  }

  data class RoleSalary(val title: String, val amount: BigDecimal, val currency: String)

  private fun handleGet(router: Router) {
    router.get("$BASE_URI/:companyId")
      .coroutineHandler { ctx ->
        val companyId = ctx.requireId("companyId")

        val salaries = database.withDsl { dsl ->
          val companyExists = dsl.selectCount()
            .from(COMPANY)
            .where(COMPANY.ID.eq(companyId))
            .fetchOneInto(Int::class.java) == 1

          if (!companyExists) {
            return@withDsl null
          }

          dsl.select(ROLE.TITLE, avg(SALARY.AMOUNT), SALARY.CURRENCY)
            .from(SALARY)
            .join(ROLE)
            .on(SALARY.ROLE_ID.eq(ROLE.ID))
            .where(SALARY.COMPANY_ID.eq(companyId))
            .groupBy(ROLE.TITLE, SALARY.CURRENCY)
            .map { (title, averageAmount, currency) ->
              RoleSalary(title, averageAmount, currency)
            }
        }

        ctx.response().jsonOrNotFound(salaries)
      }
  }
}
