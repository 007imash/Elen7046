package layer.common

import akka.actor.ActorSystem
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkasse.scaladsl.model.ServerSentEvent
import scala.concurrent.ExecutionContext.Implicits.global

object ServerSentEventSerializer extends JsonSupport  {

    def serialize(serverSentEvent: ServerSentEvent): Unit = {

        implicit val system = ActorSystem()
        implicit val mat = ActorMaterializer()

        val event = Unmarshal(serverSentEvent.data).to[Event]

        println(event)
    }
}
