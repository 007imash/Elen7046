package layer.streaming

import _root_.kafka.serializer.StringDecoder
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import layer.config.Settings
import layer.functions
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.{SparkConf, SparkContext}
import utils.SparkUtils._

import scala.util.Try
/**
  * Created by Phuti Rapheeha on 2017/06/20.
  */
object EventStreamingToCassandra {
  def main(args: Array[String]): Unit = {
    val conf =
      new SparkConf()
        .setAppName("AssetTracker")
        .setMaster("local")// <--- This is what's missing
        .set("spark.storage.memoryFraction", "1")

    val sc = new SparkContext(conf)
    val batchDuration = Seconds(4)

    val sqlContext = getSQLContext(sc)
    import sqlContext.implicits._

    def streamingApp(sc: SparkContext, batchDuration: Duration) = {
      val streamingContext = new StreamingContext(sc, batchDuration)
      val particleReaderGen = Settings.ParticleReaderGen
      val topic = particleReaderGen.kafkaTopic

      val kafkaDirectParams = Map(
        "metadata.broker.list" -> "localhost:9092",
        "group.id" -> "EventConsumer",
        "auto.offset.reset" -> "smallest"
      )

      var fromOffsets : Map[TopicAndPartition, Long] = Map.empty
      val hdfsPath = particleReaderGen.hdfsPath

      Try(sqlContext.read.parquet(hdfsPath)).foreach(hdfsData =>
        fromOffsets = hdfsData.groupBy("topic", "kafkaPartition").agg(max("untilOffset").as("untilOffset"))
          .collect().map{ row =>
          (TopicAndPartition(row.getAs[String]("topic"), row.getAs[Int]("kafkaPartition")), row.getAs[String]
            ("untilOffset").toLong + 1)
        }.toMap
      )


      val kafkaDirectStream = fromOffsets.isEmpty match {
        case true =>
          KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
            streamingContext, kafkaDirectParams, Set(topic)
          )
        case false =>
          KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](
            streamingContext, kafkaDirectParams, fromOffsets, {mmd: MessageAndMetadata[String, String] => (mmd.key(), mmd.message())}
          )

      }


      val locationStream = kafkaDirectStream.transform(input => {
        functions.jsonRddToRDDDeviceLocation(input)
      }).cache()


      locationStream.foreachRDD { rdd =>
        val locationRDD = rdd.toDF()
          .selectExpr("coreid", "published_at", "data", "ttl", "inputProps.topic as topic",
            "inputProps.kafkaPartition as kafkaPartition", "inputProps.fromOffset as fromOffset", "inputProps.untilOffset as untilOffset")

        locationRDD
          .write
          .partitionBy("topic", "kafkaPartition", "published_at")
          .mode(SaveMode.Append)
          .parquet("hdfs://localhost:9000/lambda/asset-tracker-topic/")
      }

      /*val locationStateSpec =
        StateSpec
        .function(mapLocationStateFunc)
        .timeout(Minutes(120))*/

      //val kafkaDirectStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      // streamingContext, kafkaDirectParams, Set(topic)
      //).map(_._2)
    }


    /*val coordinateStream = kafkaDirectStream.transform(input => {
        input.flatMap{
            line => Some(ServerSentEventSerializer.serializeToObject(line))

        }
    }).cache()*/

    //val ssc = SparkUtils.getStreamingContext(streamingApp, sc, batchDuration )

    //ssc.start()
    //ssc.awaitTermination()
  }
}
