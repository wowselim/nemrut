package co.selim.nemrut.ext


enum class Environment(private val stringValue: String) {

  DEV("dev"), PROD("prod");

  override fun toString(): String {
    return stringValue
  }

  companion object {

    private const val ENVIRONMENT_KEY = "NEMRUT_ENVIRONMENT"

    fun set(environment: Environment) {
      System.setProperty(ENVIRONMENT_KEY, environment.stringValue)
    }

    private fun fromString(stringValue: String?): Environment? {
      return values().firstOrNull { it.stringValue == stringValue }
    }

    fun current(): Environment {
      val value: String? = System.getProperty(ENVIRONMENT_KEY) ?: System.getenv(ENVIRONMENT_KEY)
      return fromString(value) ?: PROD
    }
  }
}
