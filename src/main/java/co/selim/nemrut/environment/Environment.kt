package co.selim.nemrut.environment


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

    private fun fromString(stringValue: String?): Environment {
      val match = entries.firstOrNull { it.stringValue == stringValue }
      return requireNotNull(match) {
        val possibleValues = entries.joinToString { env -> "'$env'" }
        "Unknown environment '$stringValue'. Must be one of [$possibleValues]."
      }
    }

    fun current(): Environment {
      val value: String = System.getProperty(ENVIRONMENT_KEY)
        ?: System.getenv(ENVIRONMENT_KEY)
        ?: return PROD

      return fromString(value)
    }
  }
}
