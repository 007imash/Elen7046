package layer.common

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JasonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val itemFormat = jsonFormat4(Event)
}

