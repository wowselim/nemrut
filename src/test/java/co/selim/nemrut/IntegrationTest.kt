package co.selim.nemrut

import io.restassured.RestAssured
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class IntegrationTest {

  @Container
  protected val pgContainer = PostgreSQLContainer("postgres:14.3")

  @BeforeEach
  protected fun setup() {
    val port = 8087

    val appConfig = with(pgContainer) {
      AppConfig(
        port,
        jdbcUrl,
        username,
        password,
      )
    }

    RestAssured.port = port
    NemrutApplication.start(appConfig)
  }

  @AfterEach
  protected fun cleanup() {
    NemrutApplication.stop()
  }
}
