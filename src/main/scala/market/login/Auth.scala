package market.login


import market.login.Auth.{Id, algorithm, layout, size}

import java.security.spec.InvalidKeySpecException
import java.security.{NoSuchAlgorithmException, SecureRandom}
import java.util
import java.util.Base64
import java.util.regex.Pattern
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


class Auth(cost: Int) {
  if (cost < 0 || cost > 30) throw new Exception(s"cost $cost")
  val random: SecureRandom = new SecureRandom

  def hash(password: Array[Char]): String = {
    val salt = new Array[Byte](size / 8)
    random.nextBytes(salt)
    val dk = pbkdf2(password, salt, 1 << cost)
    val hash = new Array[Byte](salt.length + dk.length)
    System.arraycopy(salt, 0, hash, 0, salt.length)
    System.arraycopy(dk, 0, hash, salt.length, dk.length)
    val enc = Base64.getUrlEncoder.withoutPadding
    Id + cost + '$' + enc.encodeToString(hash)
  }

  def iterations(cost: Int): Int = {
    if ((cost < 0) || (cost > 30)) throw new IllegalArgumentException("cost: " + cost)
    1 << cost
  }

  def authenticate(password: Array[Char], token: String): Boolean = {
    val m = layout.matcher(token)
    if (!m.matches) throw new IllegalArgumentException("Invalid token format")
    val iteration = iterations(m.group(1).toInt)
    val hash = Base64.getUrlDecoder.decode(m.group(2))
    val salt = util.Arrays.copyOfRange(hash, 0, size / 8)
    val check = pbkdf2(password, salt, iteration)
    var zero = 0
    for (idx <- 0 until check.length) {
      zero |= hash(salt.length + idx) ^ check(idx)
    }
    zero == 0
  }

  private def pbkdf2(password: Array[Char], salt: Array[Byte], iterations: Int) = {
    val spec = new PBEKeySpec(password, salt, iterations, size)
    try {
      val f = SecretKeyFactory.getInstance(algorithm)
      f.generateSecret(spec).getEncoded
    } catch {
      case ex: NoSuchAlgorithmException =>
        throw new IllegalStateException("Missing algorithm: " + algorithm, ex)
      case ex: InvalidKeySpecException =>
        throw new IllegalStateException("Invalid SecretKeyFactory", ex)
    }
  }
}

object Auth {
  val defaultCost = 16

  val Id = "$31$"
  val algorithm = "PBKDF2WithHmacSHA1"
  val size = 128
  val layout: Pattern = Pattern.compile("\\$31\\$(\\d\\d?)\\$(.{43})")

  def apply(cost: Int = defaultCost) = new Auth(cost)
}