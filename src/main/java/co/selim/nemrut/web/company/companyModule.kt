package co.selim.nemrut.web.company

import co.selim.nemrut.web.Controller
import org.koin.dsl.bind
import org.koin.dsl.module

val companyModule = module {
  single {
    CompanyController(get(), get())
  } bind Controller::class
}
