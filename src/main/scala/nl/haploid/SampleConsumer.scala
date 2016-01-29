package nl.haploid

import java.util.Properties
import java.util.regex.Pattern

import org.apache.kafka.clients.consumer.internals.NoOpConsumerRebalanceListener
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import scala.Console._
import scala.collection.JavaConverters._

object SampleConsumer extends App {

  val consumer = new KafkaConsumer[String, String]({
    val properties = new Properties
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-consumer")
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true.toString)
    properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000.toString)
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 6000.toString)
    properties.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, 1000.toString)
    properties
  })

  val pattern = Pattern.compile("\\w{5}-\\w{5}")
  consumer.subscribe(pattern, new NoOpConsumerRebalanceListener)

  /**
   * You can rewind the consumer to the beginning of all topics:
   * consumer.assignment.asScala.foreach(p => consumer.seekToBeginning(p))
   */

  def print(record: ConsumerRecord[String, String]) = println(s"Consumed ${record.value} from ${record.topic}")

  while (true)
    consumer.poll(100).asScala
      .foreach(print)

  /**
   * If you disable auto-commit, do:
   * consumer.commitSync()
   *
   * Message delivery guarantee is:
   * - auto-commit = true  --> at-most-once
   * - auto-commit = false --> at-least-once
   */
}
