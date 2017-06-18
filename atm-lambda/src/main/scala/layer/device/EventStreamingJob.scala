package layer.device

import _root_.kafka.serializer.StringDecoder
import layer.config.Settings
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils

object EventStreamingJob extends App{

    val conf =
        new SparkConf()
            .setAppName("AssetTracker")
            .setMaster("local")// <--- This is what's missing
            .set("spark.storage.memoryFraction", "1")

    val sc = new SparkContext(conf)

    val streamingContext = new StreamingContext(sc, Seconds(4))
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

    /*val coordinateStream = kafkaDirectStream.transform(input => {
        input.flatMap{
            line => Some(ServerSentEventSerializer.serializeToObject(line))

        }
    }).cache()*/

    kafkaDirectStream.print()
    streamingContext.start()
    streamingContext.awaitTermination()
}
