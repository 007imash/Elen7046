package layer.device

/*import java.util
import layer.config.Settings
import org.apache.kafka.clients.consumer.KafkaConsumer
import scala.collection.JavaConversions._

object EventConsumer extends App {

    val properties = new EventConsumerConfig().create()
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
}*/
