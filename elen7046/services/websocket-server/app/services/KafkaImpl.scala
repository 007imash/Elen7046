package services

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import play.api.Configuration

import scala.util.{Failure, Success, Try}


@Singleton
class KafkaImpl @Inject() (configuration: Configuration) extends Kafka {

    def maybeKafkaUrl[K](f: String => K): Try[K] = {

        configuration.getString("kafka.url").fold[Try[K]] {

            Failure(new Error("kafka.url was not set"))
        } { kafkaUrl =>

            import java.net.URI

            val kafkaUrls = kafkaUrl.split(",").map { urlString =>

                val uri = new URI(urlString)
                Seq(uri.getHost, uri.getPort).mkString(":")
            }

            Success(f(kafkaUrls.mkString(",")))
        }
    }

    def consumerSettings: Try[ConsumerSettings[String, String]] = {

        maybeKafkaUrl { kafkaUrl =>

            val system = ActorSystem("asset-tracker-service")

            ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
                .withBootstrapServers("localhost:9092")
                .withGroupId("CoordinateConsumer")
                .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        }
    }

    def source(topic: String): Try[Source[ConsumerRecord[String, String], _]] = {

        val subscriptions = Subscriptions.topics(topic)
        consumerSettings.map(Consumer.plainSource(_, subscriptions))
    }
}