package services

import akka.stream.scaladsl.Source
import org.apache.kafka.clients.consumer.ConsumerRecord

import scala.util.Try

trait Kafka {
    def source(topic: String): Try[Source[ConsumerRecord[String, String], _]]
}

