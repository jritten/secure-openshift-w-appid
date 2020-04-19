import { Component } from '@angular/core';
import {AppConfigService, AppConfig} from './app.config.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  config: AppConfig;

  constructor( private appConfigService: AppConfigService) {

    this.config = this.appConfigService.getConfig();

    console.log('bffUrl=' + this.config.bffUrl);
  }

  login() {
    // location.href = this.config.bffUrl + '/oauth2/authorization/appid';
    location.href = this.config.bffUrl + '/login';
  }

  logout() {
    location.href = this.config.bffUrl + '/logout';
  }
}
