package layer.common

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

    implicit val itemFormat: RootJsonFormat[Event] = jsonFormat4(Event)
}

