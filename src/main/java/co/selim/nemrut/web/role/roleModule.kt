package co.selim.nemrut.web.salary

import co.selim.nemrut.web.Controller
import org.koin.dsl.bind
import org.koin.dsl.module

val salaryModule = module {
  single {
    RoleController(get(), get())
  } bind Controller::class
}