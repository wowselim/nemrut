package co.selim.nemrut

import co.selim.nemrut.db.dbModule
import co.selim.nemrut.ext.Environment
import co.selim.nemrut.web.WebVerticle
import co.selim.nemrut.web.company.companyModule
import co.selim.nemrut.web.role.roleModule
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.awaitBlocking
import org.flywaydb.core.Flyway
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.slf4j.LoggerFactory
import com.fasterxml.jackson.databind.Module as JacksonDatabindModule

class MainVerticle : CoroutineVerticle(), KoinComponent {

  companion object {
    private val LOG = LoggerFactory.getLogger(MainVerticle::class.java)
  }

  private val flyway by inject<Flyway>()

  override suspend fun start() {
    val kotlinModule = kotlinModule()
    val javaTimeModule = JavaTimeModule()
    DatabindCodec.mapper().configure(kotlinModule, javaTimeModule)
    DatabindCodec.prettyMapper().configure(kotlinModule, javaTimeModule)

    System.setProperty("org.jooq.no-logo", "true")

    val vertxModule = module {
      single { vertx }
    }

    startKoin {
      modules(
        vertxModule,
        appConfigModule,
        dbModule,
        companyModule,
        roleModule,
      )
    }

    val environment = Environment.current()

    LOG.info("Started application in [$environment] mode")

    awaitBlocking {
      if (environment == Environment.DEV) {
        flyway.clean()
      }
      flyway.migrate()
    }

    val cpuCount = Runtime.getRuntime().availableProcessors()
    val deploymentOptions = DeploymentOptions().setInstances(cpuCount * 2)
    vertx.deployVerticle(WebVerticle::class.java.name, deploymentOptions).await()
  }

  override suspend fun stop() {
    stopKoin()
  }

  private fun ObjectMapper.configure(vararg modules: JacksonDatabindModule) {
    registerModules(*modules)
    //propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
  }
}

suspend fun main() {
  Environment.set(Environment.DEV)
  val vertx = Vertx.vertx()
  vertx.deployVerticle(MainVerticle::class.java.name).await()
}
