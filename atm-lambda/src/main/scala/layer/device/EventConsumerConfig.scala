package layer.device

import java.util.Properties

import layer.config.Settings
import org.apache.kafka.clients.consumer.ConsumerConfig


class EventConsumerConfig {

    def create() : Properties = {

        val properties = new Properties()

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Settings.ParticleReaderGen.kafkaBootstrapServers)
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Settings.ParticleReaderGen.kafkaKeyDeserializerClass)
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Settings.ParticleReaderGen.kafkaValueDeserializerClass)
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, Settings.ParticleReaderGen.kafkaClientId)
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, Settings.ParticleReaderGen.kafkaGroupId)

        properties
    }
}
