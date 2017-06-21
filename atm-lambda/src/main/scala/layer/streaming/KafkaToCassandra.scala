package layer.streaming

import kafka.serializer.StringDecoder
import layer.config.Settings
import layer.domain.DeviceLocation
import layer.domain.serializer.DeviceLocationFSTSerializer
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.kafka.common.serialization.StringDeserializer
import com.datastax.spark.connector.streaming._
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010._
import utils.SparkUtils._
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import _root_.kafka.serializer.StringDecoder
import org.apache.spark.streaming._


/**
  * Created by Phuti Rapheeha on 2017/06/21.
  */
object KafkaToCassandra {
  import scala.language.implicitConversions

  def main(args: Array[String]): Unit = {

    val particleReaderGen = Settings.ParticleReaderGen
    val cassandraHost = particleReaderGen.cassandraHost
    val cassandraPort = particleReaderGen.cassandraPort

    val conf =
      new SparkConf()
        .setAppName("AssetTracker")
        .setMaster("local")// <--- This is what's missing
        .set("spark.storage.memoryFraction", "1")
        .set("spark.cassandra.connection.host", cassandraHost )
        .set("spark.cassandra.connection.port", cassandraPort )
        .set("spark.cassandra.connection.keep_alive_ms", "30000")

    val sc = new SparkContext(conf)

    val streamingContext = new StreamingContext(sc, Seconds(4))
    val topic = particleReaderGen.kafkaTopic

    val kafkaDirectParams = Map(
      "metadata.broker.list" -> "localhost:9092",
      "group.id" -> "EventConsumer",
      "auto.offset.reset" -> "smallest"
    )

    /*    val kafkaDirectStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
          streamingContext, kafkaDirectParams, Set(topic)
        ).map(_._2)
*/

    val kafkaParams = Map[String, String](
      "bootstrap.servers" -> "localhost:9092",
      "group.id" -> "EventConsumer",
      "key.deserializer" -> classOf[StringDeserializer].getName,
      "value.deserializer" -> classOf[DeviceLocationFSTSerializer].getName,
      "session.timeout.ms" -> s"${1 * 60 * 1000}",
      "request.timeout.ms" -> s"${2 * 60 * 1000}",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> "false"
    )

    val kafkaStream = KafkaUtils.createDirectStream[String, DeviceLocation](
      streamingContext,
      LocationStrategies.PreferBrokers,
      ConsumerStrategies.Subscribe[String, DeviceLocation](Set(topic),kafkaParams)
    )

    val deviceLocation = kafkaStream.map { consumerRecord => consumerRecord.value}.cache()

    deviceLocation.saveToCassandra("asset_tracking_management", "device_location")

    kafkaDirectStream.print()
    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
