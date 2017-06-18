package layer.common

import akka.actor.ActorSystem
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkasse.scaladsl.model.ServerSentEvent
import scala.concurrent.ExecutionContext.Implicits.global

object ServerSentEventSerializer extends JsonSupport  {

    def serializeToObject(input: String): Event = {

        implicit val system = ActorSystem()
        implicit val mat = ActorMaterializer()

        val event = Unmarshal(input).to[Event]
        event.asInstanceOf[Event]
    }

    def serializeToString(serverSentEvent: ServerSentEvent): String = {

        serverSentEvent.data
    }
}
