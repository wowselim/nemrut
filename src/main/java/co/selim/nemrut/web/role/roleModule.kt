package co.selim.nemrut.web.role

import co.selim.nemrut.web.Controller
import org.koin.dsl.bind
import org.koin.dsl.module

val roleModule = module {
  single {
    RoleController(get(), get())
  } bind Controller::class
}
