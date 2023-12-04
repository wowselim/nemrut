package co.selim.nemrut.web.company

import co.selim.nemrut.db.Database
import co.selim.nemrut.jooq.Tables
import co.selim.nemrut.jooq.tables.pojos.Company
import co.selim.nemrut.web.*
import co.selim.nemrut.web.ext.*
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import org.jooq.impl.DSL
import java.net.URI
import java.util.*

class CompanyController(private val database: Database) : Controller() {

  companion object {
    const val BASE_URI = "/companies"
  }

  override fun register(router: Router) {
    handleGetOne(router)
    handleGet(router)
    handlePost(router)
    handlePut(router)
    handleGetSalaries(router)
  }

  data class UpsertCompanyDto(val name: String)

  private fun handleGetOne(router: Router) {
    router.get("$BASE_URI/:id")
      .coroutineHandler { ctx ->
        val id = ctx.requireId("id")
        val company = database.withDsl { dsl ->
          dsl.selectFrom(Tables.COMPANY)
            .where(Tables.COMPANY.ID.eq(id))
            .fetchOneInto(Company::class.java)
        }

        ctx.response().jsonOrNotFound(company)
      }
  }

  private fun handleGet(router: Router) {
    router.get(BASE_URI)
      .coroutineHandler { ctx ->
        val companies = database.withDsl { dsl ->
          dsl.selectFrom(Tables.COMPANY)
            .fetchInto(Company::class.java)
        }

        ctx.response().json(companies)
      }
  }

  private fun handlePost(router: Router) {
    router.post(BASE_URI)
      .handler(BodyHandler.create(false))
      .coroutineHandler { ctx ->
        val request = ctx.requireBody<UpsertCompanyDto>()

        val company = database.withTransaction { dsl ->
          dsl.insertInto(Tables.COMPANY)
            .set(dsl.newRecord(Tables.COMPANY, request))
            .returningResult(Tables.COMPANY)
            .fetchSingleInto(Company::class.java)
        }

        ctx.response().created(company, URI.create("$BASE_URI/${company.id}"))
      }
  }

  private fun handlePut(router: Router) {
    router.put("$BASE_URI/:id")
      .handler(BodyHandler.create(false))
      .coroutineHandler { ctx ->
        val id = ctx.requireId("id")
        val request = ctx.requireBody<UpsertCompanyDto>()

        val company = database.withTransaction { dsl ->
          dsl.update(Tables.COMPANY)
            .set(dsl.newRecord(Tables.COMPANY, request))
            .where(Tables.COMPANY.ID.eq(id))
            .returning()
            .fetchOneInto(Company::class.java)
        }
        ctx.response().jsonOrNotFound(company)
      }
  }

  data class RoleSalary(val title: String, val amount: Long, val currency: String)

  private fun handleGetSalaries(router: Router) {
    router.get("${BASE_URI}/:id/salaries")
      .coroutineHandler { ctx ->
        val id = ctx.requireId("id")

        val salaries = database.withDsl { dsl ->
          dsl.select(Tables.ROLE.TITLE, DSL.avg(Tables.SALARY.AMOUNT).`as`("amount"), Tables.SALARY.CURRENCY)
            .from(Tables.SALARY)
            .join(Tables.ROLE)
            .on(Tables.SALARY.ROLE_ID.eq(Tables.ROLE.ID))
            .where(Tables.SALARY.COMPANY_ID.eq(id))
            .groupBy(Tables.ROLE.TITLE, Tables.SALARY.CURRENCY)
            .fetchInto(RoleSalary::class.java)
        }

        ctx.response().json(salaries)
      }
  }
}
