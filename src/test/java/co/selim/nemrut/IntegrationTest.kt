package co.selim.nemrut

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class IntegrationTest {

  @Container
  protected val pgContainer = PostgreSQLContainer<Nothing>("postgres:14.3")

  @BeforeEach
  protected open fun setup() {
    appConfigModule.single {
      AppConfig(
        8080,
        pgContainer.jdbcUrl,
        pgContainer.username,
        pgContainer.password,
      )
    }

    NemrutApplication.start()
  }

  @AfterEach
  protected open fun cleanup() {
    NemrutApplication.stop()
  }
}
