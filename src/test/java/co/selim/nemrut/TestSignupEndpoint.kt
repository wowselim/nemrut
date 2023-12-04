package co.selim.nemrut

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Test
import co.selim.nemrut.web.auth.SigninHandler.Companion.BASE_URI as SIGNIN_URI
import co.selim.nemrut.web.auth.SignupHandler.Companion.BASE_URI as SIGNUP_URI

class TestSignupEndpoint : IntegrationTest() {

  private val requestBody = mapOf(
    "username" to "janedoe",
    "password" to "s3cr3t",
    )

  @Test
  fun `signup returns 200 and login works`() {
    Given {
      body(requestBody)
    } When {
      post(SIGNUP_URI)
    } Then {
      statusCode(200)
    }

    Given {
      body(requestBody)
    } When {
      post(SIGNIN_URI)
    } Then {
      statusCode(200)
    }
  }
}
