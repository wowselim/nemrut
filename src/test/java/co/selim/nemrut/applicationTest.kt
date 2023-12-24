package co.selim.nemrut

import co.selim.nemrut.environment.Environment
import com.github.michaelbull.logging.InlineLogger
import kotlinx.coroutines.delay
import org.testcontainers.containers.PostgreSQLContainer
import java.util.concurrent.TimeUnit

suspend fun main() {
  val log = InlineLogger()

  val pgContainer = PostgreSQLContainer("postgres:latest")
  pgContainer.start()
  pgContainer.use {
    val config = AppConfig(
      httpPort = 8080,
      dbUrl = it.jdbcUrl,
      dbUsername = it.username,
      dbPassword = it.password,
    )
    log.info {
      """Started database {"url": "${it.jdbcUrl}", "username": "${it.username}", "password": "${it.password}"}"""
    }

    Environment.set(Environment.DEV)
    NemrutApplication.start(config)
    delay(TimeUnit.DAYS.toMillis(365))
  }
}
