package layer.batch

import layer.config.Settings
import utils.SparkUtils

object BatchJob {
 def main(args:Array[String]) : Unit = {

   //setup spark context
   val sc = SparkUtils.getSparkContext("Lambda with Spark")
   val sqlContext = SparkUtils.getSQLContext(sc)
   val plc = Settings.ParticleReaderGen

   //initialize input RDD
   val inputDF = sqlContext.read.parquet(plc.hdfsPath)
     .where("unix_timestamp() - timestamp_hour / 1000 <= 60 * 60 * 6")

   inputDF.registerTempTable("Location")

 }

}
