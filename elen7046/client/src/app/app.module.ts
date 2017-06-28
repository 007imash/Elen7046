import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { FooterLayoutComponent } from './footer-layout/footer-layout.component';
import { LbdModule } from './lbd/lbd.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TableComponent } from './table/table.component';
import { MapsComponent } from './maps/maps.component';
import { AssetService } from './service/asset.service'
import { ServerSocketService } from './service/server-socket.service'
import { AgmCoreModule } from '@agm/core';
import { WebSocketService } from 'angular2-websocket-service';

const appRoutes: Routes = [
  { path: 'maps', component: MapsComponent },
  {
    path: '', component: FooterLayoutComponent, children:
    [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'table', component: TableComponent },
      { path: '**', redirectTo: 'dashboard' }
    ]
  }
];

@NgModule({
  declarations: [
    AppComponent,
    FooterLayoutComponent,
    DashboardComponent,
    TableComponent,
    MapsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(appRoutes),
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyA6NGXwYveTw4QVhdIqcx9VyoaheSiEXFQ'
    }),
    LbdModule
  ],
  bootstrap: [AppComponent],
  providers: [AssetService,ServerSocketService,WebSocketService]

})
export class AppModule { }
