package co.selim.nemrut

import co.selim.nemrut.web.auth.SigninController
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class IntegrationTest {

  @Container
  protected val pgContainer = PostgreSQLContainer("postgres:16")

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

  protected fun RequestSpecification.login(): RequestSpecification {
    val cookie = given()
      .body(mapOf("username" to "johndoe", "password" to "password"))
      .post(SigninController.BASE_URI)
      .then()
      .extract()
      .detailedCookie("vertx-web.session")

    return cookie(cookie).contentType(ContentType.JSON)
  }
}
