package co.selim.nemrut

import co.selim.nemrut.web.auth.LoginController.Companion.BASE_URI
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Test

class TestLoginEndpoint : IntegrationTest() {

  private fun credentialsOf(username: String, password: String): Map<String, String> {
    return mapOf("username" to username, "password" to password)
  }

  @Test
  fun `correct credentials return 200 status code`() {
    Given {
      body(credentialsOf("johndoe", "password"))
    } When {
      post(BASE_URI)
    } Then {
      statusCode(200)
    }
  }

  @Test
  fun `wrong credentials return 401 status code`() {
    Given {
      body(credentialsOf("johndoe", "s3cr3t"))
    } When {
      post(BASE_URI)
    } Then {
      statusCode(401)
    }
  }
}
