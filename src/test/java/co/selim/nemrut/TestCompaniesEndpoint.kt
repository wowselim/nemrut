package co.selim.nemrut

import co.selim.nemrut.jooq.tables.pojos.Company
import co.selim.nemrut.web.company.CompanyController.Companion.BASE_URI
import io.restassured.http.ContentType.JSON
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(VertxExtension::class)
class TestCompaniesEndpoint(vertx: Vertx) : IntegrationTest(vertx) {

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
  fun `creating a new note returns 201`() {
    Given {
      body(requestBody)
    } When {
      post(BASE_URI)
    } Then {
      statusCode(201)
        .and()
        .contentType(JSON)
    }
  }

  @Test
  fun `updating a company returns 200`() {
    val response = Given {
      body(requestBody)
    } When {
      post(BASE_URI)
    } Then {
      statusCode(201)
        .and()
        .contentType(JSON)
    }

    val company = response.extract().body().`as`(Company::class.java)
    Given {
      body(company)
    } When {
      put("$BASE_URI/${company.id}")
    } Then {
      statusCode(200)
        .and()
        .contentType(JSON)
    }
  }

  @Test
  fun `updating a company that doesn't exist returns 404`() {
    Given {
      body(requestBody)
    } When {
      put("$BASE_URI/${UUID.randomUUID()}")
    } Then {
      statusCode(404)
    }
  }

  @Test
  fun `getting company by id returns that company`() {
    val response = Given {
      body(requestBody)
    } When {
      post(BASE_URI)
    } Then {
      statusCode(201)
        .and()
        .contentType(JSON)
    }

    val company = response.extract().body().`as`(Company::class.java)
    When {
      get("$BASE_URI/${company.id}")
    } Then {
      statusCode(200)
        .and()
        .contentType(JSON)
    }
  }
}
