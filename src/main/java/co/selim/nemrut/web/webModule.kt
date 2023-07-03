package co.selim.nemrut.web

import co.selim.nemrut.AppConfig
import co.selim.nemrut.db.Database
import co.selim.nemrut.web.company.CompanyController
import co.selim.nemrut.web.role.RoleController
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import java.util.function.Supplier
import javax.inject.Singleton

@Module
class WebModule {

  @Provides
  @Singleton
  @IntoSet
  fun companyController(vertx: Vertx, database: Database): Controller {
    return CompanyController(vertx, database)
  }

  @Provides
  @Singleton
  @IntoSet
  fun roleController(vertx: Vertx, database: Database): Controller {
    return RoleController(vertx, database)
  }

  @Provides
  @IntoSet
  fun webVerticle(
    appConfig: AppConfig,
    controllers: Set<@JvmSuppressWildcards Controller>
  ): Supplier<Verticle> = Supplier { WebVerticle(appConfig, controllers) }
}
