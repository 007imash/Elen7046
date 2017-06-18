package layer.steaming
//import breeze.linalg.max
import io.netty.handler.codec.string.StringDecoder
import kafka.common.TopicAndPartition
import layer.config.Settings
import layer.functions
import org.apache.spark.SparkContext
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions._
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
import utils.SparkUtils
/**
  * Created by Phuti Rapheeha on 2017/06/18.
  */
object StreamingJob {
  def main(args: Array[String]): Unit = {
    // setup Spark Context
    val sc = SparkUtils.getSparkContext("Lambda with Spark")
    val sqlContext = SparkUtils.getSQLContext(sc)
    import sqlContext.implicits._


    val batchDuration = Seconds(4)

    def streamingApp(sc: SparkContext, batchDuration: Duration) = {
      val ssc = new StreamingContext(sc, batchDuration)
      val plc = Settings.ParticleReaderGen
      val topic = plc.kafkaTopic

      /*val inputPath = SparkUtils.isIDE match {
        case true => "file:///c:/hdfs/input"
        case false => "file:///c:/hdfs/input"
      }*/

      val kafkaDirectionParams = Map(
        "metadata.broker.list" -> "localhost:9092",
        "group.id" -> "lambda",
        "auto.offset.reset" -> "largest"
      )

      val hdfsPath = plc.hdfsPath
      val hdfsData = sqlContext.read.parquet(hdfsPath)

      val fromOffsets = hdfsData.groupBy("topic", "kafkaPartition").agg(max("untilOffset").as("untilOffset"))
        .collect().map { row =>
        (TopicAndPartition(row.getAs[String]("topic"), row.getAs[Int]("kafkaPartition")), row.getAs[String]("untilOffset").toLong + 1)
      }.toMap

      val kafkaDirectStream = fromOffsets.isEmpty match {
        case true =>
          KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
            ssc, kafkaDirectionParams, Set(topic)
          )
        case false =>
          KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](
            ssc, kafkaDirectionParams, fromOffsets, {mmd => (mmd.key(), mmd.message())}
          )

      }


      val locationStream = kafkaDirectStream.transform(input => {
        functions.jsonRddToRDDDeviceLocation(input)
      }).cache()

      locationStream.foreachRDD { rdd =>
        val locationRDD = rdd
          .toDF()
          .selectExpr("coreid", "published_at", "data", "ttl", "inputProps.topic as topic",
            "inputProps.kafkaPartition as kafkaPartition", "inputProps.fromOffset as fromOffset", "inputProps.untilOffset as untilOffset")

        locationRDD
          .write
          .partitionBy("topic", "kafkaPartition", "published_at")
          .mode(SaveMode.Append)
          .parquet("hdfs://localhost:9000/lambda/asset-tracker-topic/")
      }

    }

    val ssc = SparkUtils.getStreamingContext(streamingApp, sc, batchDuration )

    ssc.start()
    ssc.awaitTermination()
  }
}
