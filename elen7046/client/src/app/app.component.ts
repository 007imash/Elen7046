import { Component, OnInit } from '@angular/core';
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { HttpModule } from "@angular/http";
import { BrowserModule } from "@angular/platform-browser";
import { RouterModule, Routes } from "@angular/router";
import { IAsset } from  "./interface/asset.interface";
import { IHotspot } from  "./interface/hotspot.interface";
import { Observable } from 'rxjs';
import 'rxjs/add/operator/catch';
import { AssetService } from './service/asset.service'


@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.css']
})
export class AppComponent implements OnInit{
  title: string = 'Asset Management System';
  lat: number = -25.966972;
  lng: number = 28.1050062;
  infoWindowOpened = null;
  assets: IAsset[];
  hotspots: IHotspot[];
  
  constructor(private assetService : AssetService) { 
      
  }

ngOnInit() {
    this.assetService.getAssets().subscribe(a => {this.assets = a});
    this.assetService.getHotSpots().subscribe(h =>{this.hotspots=h});
}
clickedMarker(label: string, infoWindow, index: number) {

    if( this.infoWindowOpened ===  infoWindow)
      return;
      
    if(this.infoWindowOpened !== null)
      this.infoWindowOpened.close();
      
    this.infoWindowOpened = infoWindow;
  }

}

const routes: Routes = [
];
