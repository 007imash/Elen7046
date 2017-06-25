package utils

import java.lang.management.ManagementFactory

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Phuti Rapheeha on 2017/06/18.
  */
object SparkUtils {
val isIDE = {
  ManagementFactory.getRuntimeMXBean.getInputArguments.toString.contains("IntelliJ IDEA")
}

  def getSparkContext(appName: String) = {
    var checkPointDirectory = ""

    val conf = new SparkConf()
      .setAppName(appName)
      .set("spark.cassandra.connection.host", "localhost")

    //Check if runnning from IntelliJ
    if(isIDE) {
      System.setProperty("hadoop.home.dir", "C:\\winutils")
      conf.setMaster("local[*]")
      checkPointDirectory = "file:///c:/temp"
    }
    else
      {
        checkPointDirectory = "hdfs://localhost:9000/spark/checkpoint"
      }

    //setup spark context
    val sc = SparkContext.getOrCreate(conf)
    sc.setCheckpointDir(checkPointDirectory)
    sc
  }

  def getSQLContext(sc: SparkContext) = {
    val sqlContext = SparkSession.builder().appName(sc.appName).master("local").getOrCreate()
    sqlContext
  }

  def getStreamingContext(streamingApp: (SparkContext, Duration) => StreamingContext, sc: SparkContext, batchDuration : Duration) = {
    val creatingFunction = () => streamingApp(sc, batchDuration)
    val ssc = sc.getCheckpointDir match {
      case Some(checkPointDirectory) => StreamingContext.getActiveOrCreate(checkPointDirectory, creatingFunction, sc.hadoopConfiguration,createOnError = true)
      case None => StreamingContext.getActiveOrCreate(creatingFunction)
    }
    sc.getCheckpointDir.foreach(cp => ssc.checkpoint(cp))
    ssc
  }
}
