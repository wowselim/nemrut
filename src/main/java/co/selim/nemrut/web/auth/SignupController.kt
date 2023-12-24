package co.selim.nemrut.web.auth

import co.selim.nemrut.db.Database
import co.selim.nemrut.jooq.Tables
import co.selim.nemrut.web.Controller
import co.selim.nemrut.web.ext.requireBody
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.michaelbull.logging.InlineLogger
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.coAwait
import java.util.*

class SignupController(private val database: Database) : Controller() {

  companion object {
    const val BASE_URI = "/signup"
    private val LOG = InlineLogger()
  }

  override fun register(router: Router) {
    handlePost(router)
  }

  @Suppress("PropertyName")
  data class SignupDto(val username: String, @JsonProperty("password") val _password: String)

  private fun handlePost(router: Router) {
    router.post(BASE_URI)
      .handler(BodyHandler.create(false))
      .coroutineHandler { ctx ->
        val body = ctx.requireBody<SignupDto>()
        val password = Hashing.hash(body._password)
        val id = database.withTransaction { dsl ->
          dsl.insertInto(Tables.USER_ACCOUNT)
            .set(Tables.USER_ACCOUNT.USERNAME, body.username)
            .set(Tables.USER_ACCOUNT.PASSWORD, password)
            .returningResult(Tables.USER_ACCOUNT.ID)
            .fetchSingleInto(UUID::class.java)
        }.coAwait()
        LOG.info { "Created user account '${body.username}' with id '$id'" }
        ctx.end().coAwait()
      }
  }
}
