package layer.domain.serializer
import java.util

import layer.domain.DeviceLocation
import org.apache.kafka.common.serialization.Serializer
import org.nustaq.serialization.FSTConfiguration
/**
  * Created by Phuti Rapheeha on 2017/06/22.
  */

object DeviceLocationFSTSerializer{
  val fst = FSTConfiguration.createDefaultConfiguration()
}
class DeviceLocationFSTSerializer extends Serializer[DeviceLocation]{
  import DeviceLocationFSTSerializer._

  override def serialize(topic: String, data: DeviceLocation): Array[Byte] = {
    fst.asByteArray(data)
  }

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def close(): Unit = {}
}
