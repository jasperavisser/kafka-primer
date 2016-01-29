package nl.haploid

import scala.concurrent.duration.FiniteDuration
import scala.util.Random

trait Sleeper {
  private[this] val Consonants = "bcdfghjklmnprstvwxz".toCharArray
  private[this] val Vowels = "aeiou".toCharArray

  private[this] def draw[A](c: Array[A]): A = c(Random.nextInt(c.length))

  def randomTopic(length: Int = 10): String =
    (1 to length / 2).flatMap(i => List(draw(Consonants), draw(Vowels))).mkString("").substring(0, length).splitAt(length / 2).productIterator.toList.mkString("-")

  def sleep(duration: FiniteDuration) = Thread.sleep(duration.toMillis)
}
