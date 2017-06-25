import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ApplicationRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { AssetService } from './service/asset.service'
import { ServerSocketService } from './service/server-socket.service'
import { AgmCoreModule } from '@agm/core';
import { WebSocketService } from 'angular2-websocket-service';

@NgModule({
  imports: [
    BrowserModule,
    CommonModule,
    FormsModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyA6NGXwYveTw4QVhdIqcx9VyoaheSiEXFQ'
    })
  ],
  providers: [ AssetService,ServerSocketService,WebSocketService],
  declarations: [ AppComponent ],
  bootstrap: [ AppComponent ]
})
export class AppModule {}