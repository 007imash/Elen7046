package layer.streaming

import _root_.kafka.serializer.StringDecoder
import com.datastax.spark.connector.streaming._
import layer.config.Settings
import layer.device.{EventJsonSerializer, EventProducer}
import layer.domain.{Coordinate, CoordinateJsonSerializer}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}


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

    val kafkaDirectStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      streamingContext, kafkaDirectParams, Set(topic)
    ).map(_._2)


    val coordinateStream = kafkaDirectStream.transform(input => {

      input.flatMap{ line =>

        val isValid = EventJsonSerializer.tryParse(line)

        if(isValid){

          val event = EventJsonSerializer.deserializeEvent(line)
          val coordinates = event.data.split(",")

          if(event.coreid == "5a003d001451343334363036" &&
            coordinates(0) != "offline" &&
            coordinates(0) != "online") {

            val coordinate = Coordinate(coordinates(0).toDouble,
              coordinates(1).toDouble, event.published_at, event.coreid)

            EventProducer.produceMessage(CoordinateJsonSerializer.serialize(coordinate), "test-topic")

            Some(coordinate)
          }
          else{
            None
          }
        }
        else {
          None
        }
      }
    }).saveToCassandra("asset_tracking_management", "coordinates_speed")

    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
