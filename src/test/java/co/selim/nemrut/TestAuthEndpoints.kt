package co.selim.nemrut

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Test
import co.selim.nemrut.web.auth.SigninController.Companion.BASE_URI as SIGNIN_URI
import co.selim.nemrut.web.auth.SignupController.Companion.BASE_URI as SIGNUP_URI
import co.selim.nemrut.web.auth.SignoutController.Companion.BASE_URI as SIGNOUT_URI

class TestAuthEndpoints : IntegrationTest() {

  private fun credentialsOf(username: String, password: String): Map<String, String> {
    return mapOf("username" to username, "password" to password)
  }

  @Test
  fun `signup, signin and signout flow works as expected`() {
    val janesCredentials = credentialsOf("janedoe", "s3cr3t")

    Given {
      body(janesCredentials)
    } When {
      post(SIGNUP_URI)
    } Then {
      statusCode(200)
    }

    Given {
      body(janesCredentials)
    } When {
      post(SIGNIN_URI)
    } Then {
      statusCode(200)
    }

    Given {
      body(credentialsOf("janedoe", "kaboom"))
    } When {
      post(SIGNIN_URI)
    } Then {
      statusCode(401)
    }

    When {
      post(SIGNOUT_URI)
    } Then {
      statusCode(200)
    }
  }
}
