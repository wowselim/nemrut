package co.selim.nemrut

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.restassured.RestAssured
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.koin.dsl.module
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class IntegrationTest(private val vertx: Vertx) {

  @Container
  protected val pgContainer = PostgreSQLContainer<Nothing>("postgres:14.3")

  private var deploymentId: String? = null

  companion object {
    @JvmStatic
    @BeforeAll
    fun prepare() {
      RestAssured.config = RestAssured.config.apply {
        jacksonObjectMapper().apply {
          registerKotlinModule().registerModules(JavaTimeModule())
        }
      }
    }
  }

  @BeforeEach
  protected open fun setup() {
    appConfigModule = module {
      single {
        AppConfig(
          8080,
          pgContainer.jdbcUrl,
          pgContainer.username,
          pgContainer.password,
        )
      }
    }

    deploymentId = runBlocking {
      vertx.deployVerticle(MainVerticle::class.java, DeploymentOptions()).await()
    }
  }

  @AfterEach
  protected open fun cleanup() {
    deploymentId?.let(vertx::undeploy)
  }
}
