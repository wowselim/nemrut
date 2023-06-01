package co.selim.nemrut.web.company

import co.selim.nemrut.db.Database
import co.selim.nemrut.ext.*
import co.selim.nemrut.jooq.Tables
import co.selim.nemrut.jooq.tables.pojos.Company
import co.selim.nemrut.web.Controller
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import java.net.URI

class CompanyController(
  vertx: Vertx,
  private val database: Database
) : Controller(vertx) {

  companion object {
    const val BASE_URI = "/companies"
  }

  override fun register(router: Router) {
    handleGetOne(router)
    handleGet(router)
    handlePost(router)
    handlePut(router)
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

        val company = database.withDsl { dsl ->
          dsl.insertInto(Tables.COMPANY)
            .set(dsl.newRecord(Tables.COMPANY, request))
            .returning()
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

        val company = database.withDsl { dsl ->
          dsl.update(Tables.COMPANY)
            .set(dsl.newRecord(Tables.COMPANY, request))
            .where(Tables.COMPANY.ID.eq(id))
            .returning()
            .fetchOneInto(Company::class.java)
        }
        ctx.response().jsonOrNotFound(company)
      }
  }
}
