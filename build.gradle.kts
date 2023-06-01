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
    mavenLocal()
    mavenCentral()
  }
  dependencies {
    classpath("org.jooq:jooq-codegen:3.17.4")
    classpath("org.postgresql:postgresql:42.5.0")
    classpath("org.testcontainers:postgresql:1.17.3")
    classpath("org.flywaydb:flyway-core:9.3.0")
  }
}

task("jooqGenerate") {
  val srcMainJava = "${projectDir}/src/main/java"
  val packageName = "co.selim.nemrut.jooq"
  doFirst { File(srcMainJava).resolve(packageName.replace('.', '/')).deleteRecursively() }
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
  kotlin("jvm") version "1.8.21"
  application
  id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "co.selim"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.4.2"
val junitJupiterVersion = "5.9.3"

val mainVerticleName = "co.selim.nemrut.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")
  implementation("io.vertx:vertx-lang-kotlin")

  compileOnly("org.slf4j:slf4j-api:2.0.7")
  runtimeOnly("org.slf4j:slf4j-simple:2.0.7")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

  implementation("io.insert-koin:koin-core-jvm:3.4.0")

  implementation("io.agroal:agroal-pool:2.1")
  implementation("org.jooq:jooq:3.18.4")
  implementation("org.flywaydb:flyway-core:9.19.1")
  runtimeOnly("org.postgresql:postgresql:42.6.0")

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  testImplementation("org.testcontainers:junit-jupiter:1.18.1")
  testImplementation("org.testcontainers:postgresql:1.18.1")
  testImplementation("io.rest-assured:kotlin-extensions:5.3.0")

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
