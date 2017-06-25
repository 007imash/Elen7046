package layer.device

import layer.device.EventJsonSupport._
import spray.json._

object EventJsonSerializer {

    def deserializeEvent(input: String): Event = {

        val json = input.parseJson
        json.convertTo[Event]
    }

    def tryParse(input: String): Boolean = try {
        deserializeEvent(input)
        true
    }
    catch{
        case _: Exception =>
        false
    }
}