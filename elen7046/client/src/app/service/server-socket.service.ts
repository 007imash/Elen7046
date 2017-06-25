
import { Injectable } from '@angular/core'
import { QueueingSubject } from 'queueing-subject'
import { Observable } from 'rxjs/Observable'
import { WebSocketService } from 'angular2-websocket-service'
import { Asset } from "app/model/asset.model";

@Injectable()
export class ServerSocketService {
  private inputStream: QueueingSubject<any>
  public outputStream: Observable<any>

  constructor(private socketFactory: WebSocketService) { }

  public connect() {
    if (this.outputStream)
      return this.outputStream
    return this.outputStream = this.socketFactory.connect(
      'ws://localhost:9000/ws',
      this.inputStream = new QueueingSubject<Asset>()
    ).share();
  }
 
  public send(message: any):void {
    this.inputStream.next(message)
  }

}




// Using share() causes a single websocket to be created when the first 
    // observer subscribes. This socket is shared with subsequent observers 
    // and closed when the observer count falls to zero. 


    // If the websocket is not connected then the QueueingSubject will ensure 
    // that messages are queued and delivered when the websocket reconnects. 
    // A regular Subject can be used to discard messages sent when the websocket 
    // is disconnected. 