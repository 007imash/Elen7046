package layer.domain

import layer.domain.CoordinateJsonSupport._
import spray.json._

object CoordinateJsonSerializer {

    def deserializeEvent(input: String): Coordinate = {

        val json = input.parseJson
        json.convertTo[Coordinate]
    }

    def serialize(input: Coordinate): String = {

        input.toJson.toString()
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