package layer.device

import spray.json._

object EventJsonSupport extends DefaultJsonProtocol {

    implicit val eventFormat: RootJsonFormat[Event] = jsonFormat4(Event)

}

