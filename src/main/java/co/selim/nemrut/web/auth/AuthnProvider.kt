package co.selim.nemrut.web.auth

import co.selim.nemrut.db.Database
import co.selim.nemrut.jooq.Tables
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.User
import io.vertx.ext.auth.authentication.AuthenticationProvider
import io.vertx.ext.auth.authentication.Credentials
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials
import io.vertx.ext.web.handler.HttpException

class AuthnProvider(private val db: Database) : AuthenticationProvider {

  @Deprecated("Deprecated in Java", level = DeprecationLevel.HIDDEN)
  override fun authenticate(credentials: JsonObject, resultHandler: Handler<AsyncResult<User>>) {
    authenticate(UsernamePasswordCredentials(credentials)).onComplete(resultHandler)
  }

  @Suppress("LocalVariableName")
  override fun authenticate(_credentials: Credentials): Future<User> {
    val credentials = _credentials as UsernamePasswordCredentials

    return db.withDsl { dsl ->
      dsl.select(Tables.USER_ACCOUNT.PASSWORD)
        .from(Tables.USER_ACCOUNT)
        .where(Tables.USER_ACCOUNT.USERNAME.eq(credentials.username))
        .fetchSingleInto(String::class.java)
    }.compose { password ->
      if (!Hashing.verify(password, credentials.password)) {
        Future.failedFuture(HttpException(401))
      } else {
        val user = User.fromName(credentials.username)
        user.principal().put("amr", listOf("pwd"))
        Future.succeededFuture(user)
      }
    }
  }
}
