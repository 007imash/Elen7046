package layer.device

import java.util.Properties

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import de.heikoseeberger.akkasse.scaladsl.model.ServerSentEvent
import de.heikoseeberger.akkasse.scaladsl.unmarshalling.EventStreamUnmarshalling
import layer.config.Settings
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}

object EventProducer extends App {

    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()

    val settings = Settings.ParticleReaderGen

    import EventStreamUnmarshalling._
    import system.dispatcher

    Http()
        .singleRequest(Get(settings.deviceUrl))
        .flatMap(Unmarshal(_).to[Source[ServerSentEvent, NotUsed]])
        .foreach(_.runForeach(event => produceMessage(event.data)))

    def produceMessage (message: String): Unit = {

        val topic = Settings.ParticleReaderGen.kafkaTopic
        val properties = new Properties()

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Settings.ParticleReaderGen.kafkaBootstrapServers)
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Settings.ParticleReaderGen.kafkaKeySerializerClass)
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Settings.ParticleReaderGen.kafkaValueSerializerClass)
        properties.put(ProducerConfig.ACKS_CONFIG, Settings.ParticleReaderGen.kafkaAcks)
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, Settings.ParticleReaderGen.kafkaClientId)

        val kafkaProducer: Producer[Nothing, String] = new KafkaProducer[Nothing, String](properties)

        try {
            val producerRecord = new ProducerRecord(topic, message)
            kafkaProducer.send(producerRecord)
        }
        catch {
            case e: Exception =>
                e.printStackTrace()
        }
        finally{
            kafkaProducer.close()
        }
    }
}