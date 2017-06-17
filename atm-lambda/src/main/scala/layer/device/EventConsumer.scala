package layer.device

import java.util
import java.util.Properties

import layer.config.Settings
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import scala.collection.JavaConversions._


object EventConsumer extends App {

    val properties = new Properties()

    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Settings.ParticleReaderGen.kafkaBootstrapServers)
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Settings.ParticleReaderGen.kafkaKeyDeserializerClass)
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Settings.ParticleReaderGen.kafkaValueDeserializerClass)
    properties.put(ConsumerConfig.CLIENT_ID_CONFIG, Settings.ParticleReaderGen.kafkaClientId)
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, Settings.ParticleReaderGen.kafkaGroupId)

    val kafkaConsumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](properties)

    val topics = new util.ArrayList[String]()
    topics.add(Settings.ParticleReaderGen.kafkaTopic)

    kafkaConsumer.subscribe(topics)

    try {
        while (true) {
            val consumerRecords = kafkaConsumer.poll(10)
            for (consumerRecord <- consumerRecords) {
                println(consumerRecord.value())
            }
        }
    }
    catch {
        case e: Exception =>
            e.printStackTrace()
    }
    finally{
        kafkaConsumer.close()
    }
}
