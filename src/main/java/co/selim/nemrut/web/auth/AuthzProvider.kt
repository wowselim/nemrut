package co.selim.nemrut.web.auth

import co.selim.nemrut.db.Database
import co.selim.nemrut.jooq.Tables
import com.github.benmanes.caffeine.cache.AsyncCache
import com.github.benmanes.caffeine.cache.Caffeine
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.ext.auth.User
import io.vertx.ext.auth.authorization.Authorization
import io.vertx.ext.auth.authorization.AuthorizationProvider
import io.vertx.ext.auth.authorization.PermissionBasedAuthorization
import io.vertx.ext.auth.authorization.RoleBasedAuthorization
import java.util.concurrent.TimeUnit

class AuthzProvider(private val database: Database) : AuthorizationProvider {

  companion object {
    private val rolesCache: AsyncCache<String, List<String>> = Caffeine.newBuilder()
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .buildAsync()

    private val permissionsCache: AsyncCache<String, List<String>> = Caffeine.newBuilder()
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .buildAsync()
  }

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
      Future.failedFuture("Couldn't get username from principal")
    }
  }

  private fun getPermissions(username: String): Future<Set<Authorization>> {
    val permissionsFuture = permissionsCache.get(username) { _, _ ->
      fetchPermissions(username).toCompletionStage().toCompletableFuture()
    }
    return Future.fromCompletionStage(permissionsFuture)
      .map { permissions -> permissions.map(PermissionBasedAuthorization::create).toSet() }
  }

  private fun getRoles(username: String): Future<Set<Authorization>> {
    val rolesFuture = rolesCache.get(username) { _, _ ->
      fetchRoles(username).toCompletionStage().toCompletableFuture()
    }
    return Future.fromCompletionStage(rolesFuture)
      .map { roles -> roles.map(RoleBasedAuthorization::create).toSet() }
  }

  private fun fetchRoles(username: String): Future<List<String>> {
    return database.withDsl { dsl ->
      dsl.select(Tables.USER_ROLE.ROLE)
        .from(
          Tables.USER_ROLE.join(Tables.USER_ACCOUNT)
            .on(Tables.USER_ROLE.USER_ID.eq(Tables.USER_ACCOUNT.ID))
        )
        .where(Tables.USER_ACCOUNT.USERNAME.eq(username))
        .fetchInto(String::class.java)
    }
  }

  private fun fetchPermissions(username: String): Future<List<String>> {
    return database.withDsl { dsl ->
      dsl.select(Tables.ROLE_PERMISSION.PERMISSION)
        .from(
          Tables.ROLE_PERMISSION.join(Tables.USER_ROLE)
            .on(Tables.ROLE_PERMISSION.ROLE.eq(Tables.USER_ROLE.ROLE))
            .join(Tables.USER_ACCOUNT).on(Tables.USER_ROLE.USER_ID.eq(Tables.USER_ACCOUNT.ID))
        )
        .where(Tables.USER_ACCOUNT.USERNAME.eq(username))
        .fetchInto(String::class.java)
    }
  }
}
