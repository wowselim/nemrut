package co.selim.nemrut

import co.selim.nemrut.jooq.tables.pojos.Company
import co.selim.nemrut.web.company.CompanyController.Companion.BASE_URI
import io.restassured.http.ContentType.JSON
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import java.util.*

class TestCompaniesEndpoint : IntegrationTest() {

  @Test
  fun `get returns 200 status code`() {
    When {
      get(BASE_URI)
    } Then {
      statusCode(200)
    }
  }

  private val requestBody = mapOf("name" to "Evil Corp.")

  @Test
  fun `creating a new company without login returns 403`() {
    Given {
      body(requestBody)
    } When {
      post(BASE_URI)
    } Then {
      statusCode(401)
    }
  }

  @Test
  fun `creating a new company returns 201`() {
    Given {
      login()
        .and()
        .body(requestBody)
    } When {
      post(BASE_URI)
    } Then {
      statusCode(201)
    }
  }

  @Test
  fun `updating a company returns 200`() {
    val response = Given {
      login()
        .and()
        .body(requestBody)
    } When {
      post(BASE_URI)
    } Then {
      statusCode(201)
      contentType(JSON)
    }

    val company = response.extract().body().`as`(Company::class.java)
    Given {
      login()
        .and()
        .body(company)
    } When {
      put("$BASE_URI/${company.id}")
    } Then {
      statusCode(200)
      contentType(JSON)
      body("id", equalTo(company.id.toString()))
    }
  }

  @Test
  fun `updating a company that doesn't exist returns 404`() {
    Given {
      login()
        .and()
        .body(requestBody)
    } When {
      put("$BASE_URI/${UUID.randomUUID()}")
    } Then {
      statusCode(404)
    }
  }

  @Test
  fun `getting company by id returns that company`() {
    val response = Given {
      login()
        .and()
        .body(requestBody)
    } When {
      post(BASE_URI)
    } Then {
      statusCode(201)
      contentType(JSON)
    }

    val company = response.extract().body().`as`(Company::class.java)
    When {
      get("$BASE_URI/${company.id}")
    } Then {
      statusCode(200)
      contentType(JSON)
      body("id", equalTo(company.id.toString()))
    }
  }
}
