package co.selim.nemrut

import co.selim.nemrut.db.DatabaseModule
import co.selim.nemrut.ext.Environment
import co.selim.nemrut.web.WebModule
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.github.michaelbull.logging.InlineLogger
import dagger.Component
import dagger.Module
import dagger.Provides
import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.flywaydb.core.Flyway
import java.util.function.Supplier
import javax.inject.Singleton
import com.fasterxml.jackson.databind.Module as JacksonDatabindModule

@Module
class ApplicationModule(
  private val vertx: Vertx,
  private val appConfig: AppConfig,
) {

  @Provides
  @Singleton
  fun vertx(): Vertx = vertx

  @Provides
  @Singleton
  fun appConfig(): AppConfig = appConfig
}

@Singleton
@Component(
  modules = [
    DatabaseModule::class,
    ApplicationModule::class,
    WebModule::class,
  ]
)
interface AppComponent {

  fun vertx(): Vertx

  fun verticles(): Set<Supplier<Verticle>>

  fun flyway(): Flyway
}

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

    val appComponent = DaggerAppComponent.builder()
      .applicationModule(ApplicationModule(vertx, appConfig ?: readAppConfig(vertx)))
      .build()

    val environment = Environment.current()
    LOG.info { "Starting application in [$environment] mode" }

    with(appComponent.flyway()) {
      if (environment == Environment.DEV) {
        clean()
      }
      migrate()
    }

    val cpuCount = Runtime.getRuntime().availableProcessors()
    val deploymentOptions = DeploymentOptions().setInstances(cpuCount * 2)

    runBlocking(vertx.dispatcher()) {
      deploymentIds = appComponent.verticles()
        .map { async { vertx.deployVerticle(it, deploymentOptions).await() } }
        .awaitAll()
    }
  }

  fun stop() {
    runBlocking(vertx.dispatcher()) {
      deploymentIds.forEach { id ->
        vertx.undeploy(id).await()
      }
    }
  }

  private fun ObjectMapper.configure(vararg modules: JacksonDatabindModule) {
    registerModules(*modules)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
  }

  private fun readAppConfig(vertx: Vertx): AppConfig {
    return runBlocking(vertx.dispatcher()) {
      vertx.fileSystem()
        .readFile("config.json")
        .await()
    }
      .toJsonObject()
      .mapTo(AppConfig::class.java)
  }
}
