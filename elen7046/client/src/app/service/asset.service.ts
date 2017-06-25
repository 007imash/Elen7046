import { Injectable } from '@angular/core';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Asset } from  "../model/asset.model";
import { IHotspot } from  "../interface/hotspot.interface";
import { IAsset } from  "../interface/asset.interface";
import { Subscription } from 'rxjs/Subscription';
import { ServerSocketService } from './server-socket.service';
import { Http } from '@angular/http';
//import { Observable } from 'rxjs/Observable'
import { Observable } from 'rxjs';

@Injectable()
export class AssetService implements OnInit, OnDestroy {
  private socketSubscription: Subscription
  messages = [];
  assets: Asset[] = [];

  connection;

  message;
 
  constructor(private socket: ServerSocketService) {
    const stream = this.socket.connect()
 
    this.socketSubscription = stream.subscribe(message => {
      console.log('received message from server: ', message)
      let asset: Asset = <Asset> message; 
      asset.label='';
      asset.draggable = false;
      //if (!this.assets.includes(asset))
      if (!(this.assets.filter(e => e.deviceid == asset.deviceid).length > 0)){
        this.assets.push(asset);
      }else{
        this.assets.pop();
        this.assets.push(asset);
      }

    }) //We need to map the values we get here.
 
    this.socket.send({ type: 'helloServer' })
    //this.connection = this.socket.getMessages().subscribe(message => {

    //  this.messages.push(message);
    //  console.log('received message from server: ', message);

    //});
  }
 
  ngOnDestroy() {
    this.socketSubscription.unsubscribe()
     this.connection.unsubscribe();
  }

  getAssets()
  {
    return Observable.of(this.assets);
  }
  getHotSpots()
  {
    return Observable.of(this.hotSpots);
  }

  ngOnInit() {

    

  }


  //
	//  {
	//	  latitude: -25.966972,
	//	  longitude: 28.1050062,
  //    deviceid:'id',
  //    publishedtime:'time',
	//	  label: '',
	//	  draggable: true
	//  },
	//  {
	//	  latitude: -25.908624,
	//	  longitude: 28.1396793,
	//	  deviceid:'id',
  //    publishedtime:'time',
	//	  label: '',
	//	  draggable: true
	//  },
	//  {
	//	  latitude: -25.6645012,
	//	  longitude: 28.2427873,
	//	  deviceid:'id',
  //    publishedtime:'time',
	//	  label: '',
	//	  draggable: true
	//  },
  //    {
	//	  latitude: -25.8392153,
	//	  longitude: 28.3137568,
	//	  deviceid:'id',
  //    publishedtime:'time',
	//	  label: '',
	//	  draggable: true
	//  }
  //]
  hotSpots: IHotspot[] = [
	  {
		  lat: -25.806972,
		  lng: 28.1050062,
          color: "red"
	  },
	  {
		  lat: -25.718624,
		  lng: 28.1396793,
          color:"green"
	  },
      {
		  lat: -25.899624,
		  lng: 28.1396793,
          color:"yellow"
	  },
  ]

}
