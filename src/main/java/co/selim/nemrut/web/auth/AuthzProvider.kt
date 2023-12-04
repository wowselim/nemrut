package co.selim.nemrut.web.auth

import co.selim.nemrut.db.Database
import co.selim.nemrut.jooq.Tables
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.ext.auth.User
import io.vertx.ext.auth.authorization.Authorization
import io.vertx.ext.auth.authorization.AuthorizationProvider
import io.vertx.ext.auth.authorization.PermissionBasedAuthorization
import io.vertx.ext.auth.authorization.RoleBasedAuthorization

class AuthzProvider(private val database: Database) : AuthorizationProvider {

  override fun getId(): String = "authz"

  override fun getAuthorizations(user: User, handler: Handler<AsyncResult<Void>>) {
    getAuthorizations(user).onComplete(handler)
  }

  override fun getAuthorizations(user: User): Future<Void> {
    val username: String? = user.principal().getString("username")
    return if (username != null) {
      getRoles(username)
        .compose { roles ->
          getPermissions(username)
            .onSuccess { permissions ->
              user.authorizations().add(id, roles + permissions)
            }
        }
        .mapEmpty()
    } else {
      Future.failedFuture("Couldn't get the username from the principal")
    }
  }

  private fun getPermissions(username: String): Future<Set<Authorization>> {
    val permissions = database.withDsl { dsl ->
      dsl.select(Tables.ROLE_PERMISSION.PERMISSION)
        .from(
          Tables.ROLE_PERMISSION.join(Tables.USER_ROLE)
            .on(Tables.ROLE_PERMISSION.ROLE.eq(Tables.USER_ROLE.ROLE))
            .join(Tables.USER_ACCOUNT).on(Tables.USER_ROLE.USER_ID.eq(Tables.USER_ACCOUNT.ID))
        )
        .where(Tables.USER_ACCOUNT.USERNAME.eq(username))
        .fetchInto(String::class.java)
    }

    val authorizations = permissions.map(PermissionBasedAuthorization::create)
    return Future.succeededFuture(authorizations.toSet())
  }

  private fun getRoles(username: String): Future<Set<Authorization>> {
    val roles = database.withDsl { dsl ->
      dsl.select(Tables.USER_ROLE.ROLE)
        .from(
          Tables.USER_ROLE.join(Tables.USER_ACCOUNT)
            .on(Tables.USER_ROLE.USER_ID.eq(Tables.USER_ACCOUNT.ID))
        )
        .where(Tables.USER_ACCOUNT.USERNAME.eq(username))
        .fetchInto(String::class.java)
    }

    val authorizations = roles.map(RoleBasedAuthorization::create)
    return Future.succeededFuture(authorizations.toSet())
  }
}
