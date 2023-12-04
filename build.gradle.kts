import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
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
    mavenCentral()
  }
  dependencies {
    classpath("org.jooq:jooq-codegen:3.18.7")
    classpath("org.postgresql:postgresql:42.7.0")
    classpath("org.testcontainers:postgresql:1.19.3")
    classpath("org.flywaydb:flyway-core:9.22.3")
  }
}

task("jooqGenerate") {
  val srcMainJava = "${projectDir}/src/main/java"
  val packageName = "co.selim.nemrut.jooq"
  doFirst { File(srcMainJava).resolve(packageName.replace('.', '/')).deleteRecursively() }
  doLast {
    PostgreSQLContainer<Nothing>("postgres:16")
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
                .withDatabase(Database().withInputSchema("public"))
                .withGenerate(
                  Generate()
                    .withPojos(true)
                    .withPojosAsJavaRecordClasses(true)
                    .withPojosToString(false)
                    .withPojosEqualsAndHashCode(false)
                )
                .withTarget(
                  Target()
                    .withPackageName(packageName)
                    .withDirectory(srcMainJava)
                )
            )
        )
      }
  }
}

plugins {
  kotlin("jvm") version "1.9.21"
  application
  id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "co.selim"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

application {
  mainClass = "co.selim.nemrut.NemrutApplication"
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation(platform("io.vertx:vertx-stack-depchain:4.5.0"))
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")
  implementation("io.vertx:vertx-lang-kotlin")

  implementation("com.michael-bull.kotlin-inline-logger:kotlin-inline-logger:1.0.5")
  runtimeOnly("ch.qos.logback:logback-classic:1.4.8")
  implementation("io.reactiverse:reactiverse-contextual-logging:1.1.2")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

  implementation("io.agroal:agroal-pool:2.2")
  implementation("org.jooq:jooq:3.18.7")
  implementation("org.flywaydb:flyway-core:9.22.3")
  runtimeOnly("org.postgresql:postgresql:42.7.0")

  testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
  testImplementation("org.testcontainers:junit-jupiter:1.19.3")
  testImplementation("org.testcontainers:postgresql:1.19.3")
  testImplementation("io.rest-assured:kotlin-extensions:5.3.2")
}

tasks.withType<JavaCompile>().configureEach {
  sourceCompatibility = "${JavaVersion.VERSION_17}"
  targetCompatibility = "${JavaVersion.VERSION_17}"
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = "${JavaVersion.VERSION_17}"
    allWarningsAsErrors = true
    freeCompilerArgs = listOf("-Xcontext-receivers")
  }
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

task("buildReleaseJar") {
  dependsOn("jooqGenerate", "shadowJar")
}
