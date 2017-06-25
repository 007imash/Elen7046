package services

import com.typesafe.config.ConfigFactory

object KafkaSettings {

    private val config = ConfigFactory.load()

    object ParticleReaderGen {

        private val kafkaReaderGen = config.getConfig("kafka.consumer")

        lazy val kafkaTopic: String = kafkaReaderGen.getString("kafka_topic")
        lazy val kafkaBootstrapServers: String = kafkaReaderGen.getString("kafka_bootstrap_servers")
        lazy val kafkaKeyDeserializerClass: String = kafkaReaderGen.getString("kafka_key_deserializer_class")
        lazy val kafkaValueDeserializerClass: String = kafkaReaderGen.getString("kafka_value_deserializer_class")
        lazy val kafkaClientId: String = kafkaReaderGen.getString("kafka_client_id")
        lazy val kafkaGroupId: String = kafkaReaderGen.getString("kafka_group_id")
    }
}
