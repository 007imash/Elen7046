import {Component, OnInit} from '@angular/core';
import { NavItem, NavItemType } from './lbd/lbd.module';
import { IAsset } from "./interface/asset.interface";
import { AssetService } from "./service/asset.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  public navItems: NavItem[];
    assets: IAsset[];

  constructor(private assetService : AssetService) {
    this.assetService.getAssets().subscribe(a => {this.assets = a});
  }

  public ngOnInit(): void {
    this.navItems = [
      { type: NavItemType.Sidebar, title: 'Dashboard', routerLink: 'dashboard', iconClass: 'pe-7s-graph' },
      { type: NavItemType.Sidebar, title: 'Asset(s) Location', routerLink: 'maps', iconClass: 'pe-7s-map-marker' },
      { type: NavItemType.Sidebar, title: 'Asset List', routerLink: 'table', iconClass: 'pe-7s-note2' },
     

      {
        type: NavItemType.NavbarLeft,
        title: 'Notifications',
        iconClass: 'fa fa-globe',
        numNotifications: this.assets.length,
        dropdownItems: []
      },
      { type: NavItemType.NavbarLeft, title: 'Search', iconClass: 'fa fa-search' },

      { type: NavItemType.NavbarRight, title: 'Account' },
      
      { type: NavItemType.NavbarRight, title: 'Log out' }
    ];
  }
}
