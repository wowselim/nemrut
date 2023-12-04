package co.selim.nemrut.web.auth

import io.vertx.ext.auth.HashingStrategy
import java.security.SecureRandom
import java.util.*

object Hashing {
  private val random = SecureRandom()
  private val hashingStrategy = HashingStrategy.load()

  fun verify(hash: String, password: String): Boolean {
    return hashingStrategy.verify(hash, password)
  }

  fun hash(password: String): String {
    val salt = ByteArray(32)
    random.nextBytes(salt)

    return hashingStrategy.hash(
      "pbkdf2",
      null,
      Base64.getEncoder().encodeToString(salt),
      password
    )
  }
}
