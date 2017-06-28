import { Component, OnInit } from '@angular/core';
import { FooterItem } from '../lbd/lbd-footer/lbd-footer.component';

@Component({
  selector: 'app-footer-layout',
  templateUrl: './footer-layout.component.html'
})
export class FooterLayoutComponent implements OnInit {
  public footerItems: FooterItem[];
  public copyright: string;

  constructor() { }

  public ngOnInit() {
    this.footerItems = [
      { title: 'Home', routerLink: '' },
      { title: 'Info', routerLink: '' }
    ];
    this.copyright = '&copy; 2017 <a href="https://github.com/007imash/Elen7046">Group 04 - Elen7046</a>, crafted with love and coffee Beans';
  }
}
