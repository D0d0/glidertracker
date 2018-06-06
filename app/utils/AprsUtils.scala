package utils

object AprsUtils {
  def dmsToDeg(dms: Double): Double = {
    val absDms = Math.abs(dms)
    val d = Math.floor(absDms)
    val m = (absDms - d) * 100 / 60
    d + m
  }
}