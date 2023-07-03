package co.selim.nemrut

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
    val appConfig = with(pgContainer) {
      AppConfig(
        8080,
        jdbcUrl,
        username,
        password,
      )
    }

    NemrutApplication.start(appConfig)
  }

  @AfterEach
  protected fun cleanup() {
    NemrutApplication.stop()
  }
}
