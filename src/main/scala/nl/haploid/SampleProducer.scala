package nl.haploid

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.duration._

object SampleProducer extends App with Sleeper {

  val producer = new KafkaProducer[String, String]({
    val properties = new Properties
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.put(ProducerConfig.RETRIES_CONFIG, "3")
    properties.put(ProducerConfig.ACKS_CONFIG, "all")
    properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none")
    properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "1000")
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    properties
  })

  val topic = randomTopic()

  val messages = Stream.from(0).map(_.toString)

  def slowProducer() =
    for (message <- messages) {
      val record = new ProducerRecord[String, String](topic, message)
      producer.send(record).get
      println(s"Produced $message to $topic")
      sleep(100 milliseconds)
    }

  def fastProducer() =
    for (message <- messages)
      producer.send(new ProducerRecord[String, String](topic, message))

  slowProducer()
}
