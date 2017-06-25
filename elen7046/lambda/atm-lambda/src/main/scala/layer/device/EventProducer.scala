package layer.device

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
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}

object EventProducer extends App {

    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()

    val settings = Settings.ParticleReaderGen

    import EventStreamUnmarshalling._
    import system.dispatcher

    val events = Http()
        .singleRequest(Get(settings.deviceUrl))
        .flatMap(Unmarshal(_).to[Source[ServerSentEvent, NotUsed]])
        .foreach(_.runForeach(event => produceMessage(event.data, Settings.ParticleReaderGen.kafkaTopic)))

    def produceMessage (message: String, topic: String): Unit = {

        val properties = new EventProducerConfig().create()
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