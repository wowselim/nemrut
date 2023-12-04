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

class AuthnProvider(private val db: Database) : AuthenticationProvider {

  class AuthnException : Throwable("Invalid username / password")

  @Deprecated("Deprecated in Java", level = DeprecationLevel.HIDDEN)
  override fun authenticate(credentials: JsonObject, resultHandler: Handler<AsyncResult<User>>) {
    authenticate(UsernamePasswordCredentials(credentials)).onComplete(resultHandler)
  }

  @Suppress("LocalVariableName")
  override fun authenticate(_credentials: Credentials): Future<User> {
    val credentials = _credentials as UsernamePasswordCredentials

    try {
      val password = db.withDsl { dsl ->
        dsl.select(Tables.USER_ACCOUNT.PASSWORD)
          .from(Tables.USER_ACCOUNT)
          .where(Tables.USER_ACCOUNT.USERNAME.eq(credentials.username))
          .fetchSingleInto(String::class.java)
      }
      check(Hashing.verify(password, credentials.password))
      val user = User.fromName(credentials.username)
      user.principal().put("amr", listOf("pwd"))
      return Future.succeededFuture(user)
    } catch (t: Throwable) {
      return Future.failedFuture(AuthnException())
    }
  }
}
