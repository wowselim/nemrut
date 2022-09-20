package co.selim.nemrut

import co.selim.nemrut.jooq.Tables
import co.selim.nemrut.jooq.tables.records.NoteRecord
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.Connection
import java.sql.DriverManager

@Testcontainers
class JooqTest {

  @Container
  val pgContainer = PostgreSQLContainer<Nothing>("postgres:14.3")

  @BeforeEach
  fun setup() {
    val flyway = Flyway(
      FluentConfiguration()
        .baselineOnMigrate(true)
        .dataSource(pgContainer.jdbcUrl, pgContainer.username, pgContainer.password)
    )
    flyway.migrate()
  }

  private fun connection(): Connection {
    return DriverManager.getConnection(pgContainer.jdbcUrl, pgContainer.username, pgContainer.password)
  }

  private fun dsl(): DSLContext {
    return DSL.using(connection(), SQLDialect.POSTGRES)
  }

  @Test
  fun x() {
    dsl().insertInto(Tables.NOTE)
      .set(NoteRecord(0, "Shopping List", "Buy Milk"))
      .execute()

    dsl().selectFrom(Tables.NOTE)
      .fetch()
      .forEach { noteRecord ->
        println(noteRecord.title)
      }
  }

  @Test
  fun y() {
    println(pgContainer.jdbcUrl)
  }
}
