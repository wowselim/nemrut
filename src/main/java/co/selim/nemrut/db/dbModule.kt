package co.selim.nemrut.db

import co.selim.nemrut.AppConfig
import co.selim.nemrut.environment.Environment
import io.agroal.api.AgroalDataSource
import io.agroal.api.configuration.AgroalDataSourceConfiguration
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier
import io.agroal.api.security.NamePrincipal
import io.agroal.api.security.SimplePassword
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import java.time.Duration
import javax.sql.DataSource


fun dataSource(config: AppConfig): DataSource {
  val dataSourceConfig = AgroalDataSourceConfigurationSupplier()
    .dataSourceImplementation(AgroalDataSourceConfiguration.DataSourceImplementation.AGROAL)
    .metricsEnabled(false)
    .connectionPoolConfiguration { cp ->
      cp.validationTimeout(Duration.ofMillis(30_000L))
        .minSize(2)
        .maxSize(32)
        .initialSize(4)
        .acquisitionTimeout(Duration.ofMillis(0))
        .reapTimeout(Duration.ofMillis(0L))
        .leakTimeout(Duration.ofMillis(0L))
        .connectionFactoryConfiguration { cf ->
          cf.jdbcUrl(config.dbUrl)
            .principal(NamePrincipal(config.dbUsername))
            .credential(SimplePassword(config.dbPassword))
            .autoCommit(false)
        }
    }
    .get()

  return AgroalDataSource.from(dataSourceConfig)
}

fun flyway(dataSource: DataSource): Flyway {
  return Flyway(
    FluentConfiguration()
      .cleanDisabled(Environment.current() == Environment.PROD)
      .baselineOnMigrate(true)
      .dataSource(dataSource)
  )
}

fun database(dataSource: DataSource): Database {
  val config = DefaultConfiguration().apply {
    setDataSource(dataSource)
    setSQLDialect(SQLDialect.POSTGRES)
  }
  val dslContext = DSL.using(config)

  return object : Database {
    override fun <T> withDsl(block: (DSLContext) -> T): T {
      return block(dslContext)
    }

    override fun <T> withTransaction(block: (DSLContext) -> T): T {
      return dslContext.transactionResult { config ->
        block(config.dsl())
      }
    }
  }
}
