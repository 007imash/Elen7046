package layer.device

import java.util.Properties
import de.heikoseeberger.akkasse.scaladsl.model.ServerSentEvent
import layer.common.ServerSentEventSerializer
import layer.config.Settings
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}

object KafkaProducer {

    def sendMessage (serverSentEvent: ServerSentEvent): Unit = {

        val topic = Settings.ParticleReaderGen.kafkaTopic
        val properties = new Properties()

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Settings.ParticleReaderGen.kafkaBootstrapServers)
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Settings.ParticleReaderGen.kafkaKeySerializerClass)
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Settings.ParticleReaderGen.kafkaValueSerializerClass)
        properties.put(ProducerConfig.ACKS_CONFIG, Settings.ParticleReaderGen.kafkaAcks)
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, Settings.ParticleReaderGen.kafkaClientId)

        val kafkaProducer: Producer[Nothing, String] = new KafkaProducer[Nothing, String](properties)

        try {
            val producerRecord = new ProducerRecord(topic, ServerSentEventSerializer.serializeToString(serverSentEvent))
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
