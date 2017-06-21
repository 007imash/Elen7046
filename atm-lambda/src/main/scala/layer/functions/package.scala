package layer

import layer.domain.DeviceLocation
import org.apache.spark.rdd.RDD
import net.liftweb.json._
import org.apache.spark.streaming.kafka.HasOffsetRanges
/**
  * Created by Phuti Rapheeha on 2017/06/18.
  */
package object functions {
 def rddToRDDLocation(input: RDD[String]) = {
  input.flatMap{ line =>
   val record = line.split("\\t")
    if(record.length == 7)
     Some(DeviceLocation(record(0), record(1), record(2), record(4)))
      else
     None
  }
 }

 def jsonRddToRDDDeviceLocation(input: RDD[(String, String)]) : RDD[DeviceLocation] = {
  val offsetRanges = input.asInstanceOf[HasOffsetRanges].offsetRanges
  input.mapPartitionsWithIndex({(index, iterator) =>
    val offsetRange = offsetRanges(index)
    iterator.flatMap{kv =>
     val line = kv._2
     implicit val formats = DefaultFormats
     val jValue = parse(line)
     val location = jValue.extract[DeviceLocation]
     location.inputProps + ("topic" -> offsetRange.topic, "kafkaPartition" -> offsetRange.partition.toString,
     "fromOffset" -> offsetRange.fromOffset.toString, "untilOffset" -> offsetRange.untilOffset.toString)
     Option.apply(location)
    }

  })
 }
}
