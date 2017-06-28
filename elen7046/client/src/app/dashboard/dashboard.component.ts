import {Component, OnInit, trigger, state, style, transition, animate} from '@angular/core';
import { LegendItem, ChartType } from '../lbd/lbd-chart/lbd-chart.component';
import { Task } from '../lbd/lbd-task-list/lbd-task-list.component';
import {NotificationService, NotificationOptions} from '../lbd/services/notification.service';
import { NavbarTitleService } from '../lbd/services/navbar-title.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  animations: [
    trigger('cardemail', [
      state('*', style({
        '-ms-transform': 'translate3D(0px, 0px, 0px)',
        '-webkit-transform': 'translate3D(0px, 0px, 0px)',
        '-moz-transform': 'translate3D(0px, 0px, 0px)',
        '-o-transform': 'translate3D(0px, 0px, 0px)',
        transform: 'translate3D(0px, 0px, 0px)',
        opacity: 1})),
      transition('void => *', [
        style({opacity: 0,
          '-ms-transform': 'translate3D(0px, 150px, 0px)',
          '-webkit-transform': 'translate3D(0px, 150px, 0px)',
          '-moz-transform': 'translate3D(0px, 150px, 0px)',
          '-o-transform': 'translate3D(0px, 150px, 0px)',
          transform: 'translate3D(0px, 150px, 0px)',
        }),
        animate('0.3s 0s ease-out')
      ])
    ]),    
    trigger('cardtasks', [
      state('*', style({
        '-ms-transform': 'translate3D(0px, 0px, 0px)',
        '-webkit-transform': 'translate3D(0px, 0px, 0px)',
        '-moz-transform': 'translate3D(0px, 0px, 0px)',
        '-o-transform': 'translate3D(0px, 0px, 0px)',
        transform: 'translate3D(0px, 0px, 0px)',
        opacity: 1})),
      transition('void => *', [
        style({opacity: 0,
          '-ms-transform': 'translate3D(0px, 150px, 0px)',
          '-webkit-transform': 'translate3D(0px, 150px, 0px)',
          '-moz-transform': 'translate3D(0px, 150px, 0px)',
          '-o-transform': 'translate3D(0px, 150px, 0px)',
          transform: 'translate3D(0px, 150px, 0px)',
        }),
        animate('0.3s 0.75s ease-out')
      ])
    ])
  ]
})
export class DashboardComponent implements OnInit {
  public emailChartType: ChartType;
  public emailChartData: any;
  public emailChartLegendItems: LegendItem[];
  public tasks: Task[];

  constructor(private navbarTitleService: NavbarTitleService, private notificationService: NotificationService) { }

  public ngOnInit() {
    this.navbarTitleService.updateTitle('Dashboard');

    //this.notificationService.notify(new NotificationOptions({
    //  message: 'Welcome to Asset tracking management system.',
    //  icon: 'pe-7s-gift'
    //}));

    this.emailChartType = ChartType.Pie;
    this.emailChartData = {
      labels: ['62%', '32%', '6%'],
      series: [62, 32, 6]
    };
    this.emailChartLegendItems = [
      { title: 'Active', imageClass: 'fa fa-circle text-info' },
      { title: 'Off-line', imageClass: 'fa fa-circle text-danger' },
      { title: 'Idle', imageClass: 'fa fa-circle text-warning' }
    ];

    
    this.tasks = [
      { title: '', checked: true },
      { title: '', checked: true },
      {
        title: '',
        checked: true
      },
      { title: '', checked: true },
      { title: '', checked: false },
      { title: '', checked: false },
    ];
  }
}
