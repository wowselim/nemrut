package co.selim.nemrut.web

import co.selim.nemrut.AppConfig
import co.selim.nemrut.web.company.companyModule
import co.selim.nemrut.web.role.roleModule
import org.koin.dsl.module

val webModule = module {
  includes(
    companyModule,
    roleModule
  )

  factory {
    WebVerticle(get<AppConfig>(), getAll<Controller>())
  }
}
