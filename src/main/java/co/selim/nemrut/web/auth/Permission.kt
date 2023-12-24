package co.selim.nemrut.web.auth

import io.vertx.ext.auth.authorization.PermissionBasedAuthorization

sealed interface Permission {

  val authorization: PermissionBasedAuthorization

  data object WriteCompany : Permission {
    override val authorization: PermissionBasedAuthorization = PermissionBasedAuthorization.create("write-company")
  }
}
