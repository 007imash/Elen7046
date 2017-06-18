package layer.device


import java.util.Properties
import layer.config.Settings
import org.apache.kafka.clients.producer.ProducerConfig

class EventProducerConfig {

    def create() : Properties = {

        val properties = new Properties()

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Settings.ParticleReaderGen.kafkaBootstrapServers)
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Settings.ParticleReaderGen.kafkaKeySerializerClass)
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Settings.ParticleReaderGen.kafkaValueSerializerClass)
        properties.put(ProducerConfig.ACKS_CONFIG, Settings.ParticleReaderGen.kafkaAcks)
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, Settings.ParticleReaderGen.kafkaClientId)

        properties
    }
}
