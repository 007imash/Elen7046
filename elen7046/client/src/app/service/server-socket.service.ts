
import { Injectable } from '@angular/core'
import { QueueingSubject } from 'queueing-subject'
import { Observable } from 'rxjs/Observable'
import { WebSocketService } from 'angular2-websocket-service'
import { Asset } from "../model/asset.model";

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
