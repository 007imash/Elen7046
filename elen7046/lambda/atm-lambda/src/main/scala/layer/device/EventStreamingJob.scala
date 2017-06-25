package layer.device

import _root_.kafka.serializer.StringDecoder
import layer.config.Settings
import layer.domain.{Coordinate, CoordinateJsonSerializer}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.{SparkConf, SparkContext}

object EventStreamingJob extends App{

    val conf = new SparkConf()
            .setAppName("AssetTracker")
            .setMaster("local")
            .set("spark.storage.memoryFraction", "1")

    val sc = new SparkContext(conf)

    val streamingContext = new StreamingContext(sc, Seconds(1))
    val particleReaderGen = Settings.ParticleReaderGen
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
    }).print()

    streamingContext.start()
    streamingContext.awaitTermination()
}
