package layer.device

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import de.heikoseeberger.akkasse.scaladsl.model.ServerSentEvent
import de.heikoseeberger.akkasse.scaladsl.unmarshalling.EventStreamUnmarshalling
import layer.config.Settings

object EventProducer {

    def main(args: Array[String]): Unit = {

        implicit val system = ActorSystem()
        implicit val mat = ActorMaterializer()

        val settings = Settings.ParticleReaderGen

        import EventStreamUnmarshalling._
        import system.dispatcher

        Http()
            .singleRequest(Get(settings.deviceUrl))
            .flatMap(Unmarshal(_).to[Source[ServerSentEvent, NotUsed]])
            .foreach(_.runForeach(println))
    }
}