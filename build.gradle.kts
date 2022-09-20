import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.github.jklingsporn.vertx.jooq.generate.VertxGeneratorStrategy
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Target
import org.testcontainers.containers.PostgreSQLContainer

buildscript {
  repositories {
    mavenLocal()
    mavenCentral()
  }
  dependencies {
    classpath("io.github.jklingsporn:vertx-jooq-generate:6.5.5")
    classpath("org.postgresql:postgresql:42.5.0")
    classpath("org.testcontainers:postgresql:1.17.3")
    classpath("org.flywaydb:flyway-core:9.3.0")
  }
}

task("jooqGenerate") {
  doLast {
    PostgreSQLContainer<Nothing>("postgres:14.3")
      .apply { start() }
      .use { pgContainer ->
        val flyway = Flyway(
          FluentConfiguration()
            .locations("filesystem:src/main/resources/db/migration")
            .baselineOnMigrate(true)
            .dataSource(pgContainer.jdbcUrl, pgContainer.username, pgContainer.password)
        )
        flyway.migrate()

        GenerationTool.generate(
          Configuration()
            .withJdbc(
              Jdbc()
                .withDriver(pgContainer.driverClassName)
                .withUrl(pgContainer.jdbcUrl)
                .withUser(pgContainer.username)
                .withPassword(pgContainer.password)
            )
            .withGenerator(
              Generator()
                .withName("io.github.jklingsporn.vertx.jooq.generate.classic.ClassicReactiveVertxGenerator")
                .withDatabase(Database().withInputSchema("public"))
                .withGenerate(Generate())
                .withTarget(
                  Target()
                    .withPackageName("co.selim.nemrut.jooq")
                    .withDirectory("${projectDir}/src/main/java")
                )
                .withStrategy(Strategy().withName("io.github.jklingsporn.vertx.jooq.generate.VertxGeneratorStrategy"))
            )
        )
      }
  }
}

plugins {
  kotlin("jvm") version "1.7.10"
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "co.selim"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.3.3"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "co.selim.nemrut.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-pg-client")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")
  implementation("io.vertx:vertx-lang-kotlin")
  implementation(kotlin("stdlib-jdk8"))
  implementation("org.flywaydb:flyway-core:9.3.0")
  implementation("org.postgresql:postgresql:42.5.0")
  implementation("io.github.jklingsporn:vertx-jooq-classic-reactive:6.5.5")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  testImplementation("org.testcontainers:junit-jupiter:1.17.3")
  testImplementation("org.testcontainers:postgresql:1.17.3")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf(
    "run",
    mainVerticleName,
    "--redeploy=$watchForChange",
    "--launcher-class=$launcherClassName",
    "--on-redeploy=$doOnChange"
  )
}
