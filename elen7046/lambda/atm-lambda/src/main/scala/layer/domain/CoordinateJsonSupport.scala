package layer.domain

import spray.json._

object CoordinateJsonSupport extends DefaultJsonProtocol {

    implicit val eventFormat: RootJsonFormat[Coordinate] = jsonFormat4(Coordinate)

}
