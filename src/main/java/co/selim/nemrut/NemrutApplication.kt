package co.selim.nemrut

import co.selim.nemrut.db.dataSource
import co.selim.nemrut.db.database
import co.selim.nemrut.db.flyway
import co.selim.nemrut.environment.Environment
import co.selim.nemrut.web.WebVerticle
import co.selim.nemrut.web.auth.*
import co.selim.nemrut.web.company.CompanyController
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.github.michaelbull.logging.InlineLogger
import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.function.Supplier
import com.fasterxml.jackson.databind.Module as JacksonDatabindModule


object NemrutApplication {

  private val LOG = InlineLogger()

  @JvmStatic
  fun main(args: Array<String>) {
    start()
  }

  private lateinit var vertx: Vertx
  private lateinit var deploymentIds: List<String>

  fun start(appConfig: AppConfig? = null) {
    vertx = Vertx.vertx()
    val kotlinModule = kotlinModule()
    val javaTimeModule = JavaTimeModule()
    DatabindCodec.mapper().configure(kotlinModule, javaTimeModule)
    DatabindCodec.prettyMapper().configure(kotlinModule, javaTimeModule)

    System.setProperty("org.jooq.no-logo", "true")
    System.setProperty("org.jooq.no-tips", "true");

    val environment = Environment.current()
    LOG.info { "Starting application in [$environment] mode" }

    val config = appConfig ?: runBlocking(vertx.dispatcher()) { AppConfig.load(vertx) }
    val dataSource = dataSource(config)
    with(flyway(dataSource)) {
      if (environment == Environment.DEV) {
        clean()
      }
      migrate()
    }
    val database = database(vertx, dataSource)

    val cpuCount = Runtime.getRuntime().availableProcessors()
    val deploymentOptions = DeploymentOptions().setInstances(cpuCount * 2)

    runBlocking(vertx.dispatcher()) {
      deploymentIds = listOf<Supplier<Verticle>>(
        Supplier {
          val authnProvider = AuthnProvider(database)
          val authzProvider = AuthzProvider(database)
          val controllers = setOf(
            SignupController(database),
            SigninController(authnProvider),
            SignoutController(),
            CompanyController(database, authzProvider),
          )
          WebVerticle(
            config,
            controllers,
          )
        },
      )
        .map { async { vertx.deployVerticle(it, deploymentOptions).coAwait() } }
        .awaitAll()
    }
  }

  fun stop() {
    runBlocking(vertx.dispatcher()) {
      deploymentIds.forEach { id ->
        vertx.undeploy(id).coAwait()
      }
    }
  }

  private fun ObjectMapper.configure(vararg modules: JacksonDatabindModule) {
    registerModules(*modules)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
  }
}
