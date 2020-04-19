import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {AppConfigService, AppConfig} from './app.config.service';


class Greeting {
  id = '';
  content = '';
  hostName = '';
}

@Component({
  templateUrl: './details.component.html'
})


export class DetailsComponent implements OnInit {

  title = 'Demo';
  greeting: Greeting = new Greeting();

  constructor(private http: HttpClient, private appConfigService: AppConfigService) {
  }

  ngOnInit(): void {
    const config: AppConfig = this.appConfigService.getConfig();
    const detailsURLPath = config.bffUrl + '/details';

    console.log('detailsURLPath=' + detailsURLPath);

    this.http.get<Greeting>(detailsURLPath, {withCredentials: true})
    .subscribe(response => {
      console.log('DetailsComponent', response);
      this.greeting = response;
    });
  }
}
