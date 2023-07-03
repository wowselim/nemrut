package co.selim.nemrut

import co.selim.nemrut.db.dbModule
import co.selim.nemrut.ext.Environment
import co.selim.nemrut.web.WebVerticle
import co.selim.nemrut.web.webModule
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.runBlocking
import org.flywaydb.core.Flyway
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.slf4j.LoggerFactory
import com.fasterxml.jackson.databind.Module as JacksonDatabindModule

class NemrutApplication {

  companion object {
    private val LOG = LoggerFactory.getLogger(NemrutApplication::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
      start()
    }

    private lateinit var vertx: Vertx
    private lateinit var deploymentId: String

    fun start() {
      vertx = Vertx.vertx()
      val kotlinModule = kotlinModule()
      val javaTimeModule = JavaTimeModule()
      DatabindCodec.mapper().configure(kotlinModule, javaTimeModule)
      DatabindCodec.prettyMapper().configure(kotlinModule, javaTimeModule)

      System.setProperty("org.jooq.no-logo", "true")

      val vertxModule = module {
        single { vertx }
      }

      val koin = startKoin {
        modules(
          vertxModule,
          appConfigModule,
          dbModule,
          webModule,
        )
      }.koin

      val environment = Environment.current()
      LOG.info("Started application in [$environment] mode")

      with(koin.get<Flyway>()) {
        if (environment == Environment.DEV) {
          clean()
        }
        migrate()
      }

      val cpuCount = Runtime.getRuntime().availableProcessors()
      val deploymentOptions = DeploymentOptions().setInstances(cpuCount * 2)

      vertx.deployVerticle({ koin.get<WebVerticle>() }, deploymentOptions)
        .onSuccess { id -> deploymentId = id }
        .onFailure { t ->
          vertx.close()
            .onComplete { LOG.error("Failed to start application", t) }
        }
    }

    fun stop() {
      runBlocking(vertx.dispatcher()) {
        vertx.undeploy(deploymentId).await()
        stopKoin()
      }
    }

    private fun ObjectMapper.configure(vararg modules: JacksonDatabindModule) {
      registerModules(*modules)
      disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }
  }
}
