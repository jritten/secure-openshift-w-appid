import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { UserState } from './user-state';
import {AppConfigService, AppConfig} from './app.config.service';
@Injectable()
export class AuthService {
  userState: UserState;
  private isAuthenticatedUrl: string;

  constructor(private http: HttpClient, private appConfigService: AppConfigService) {
    const config: AppConfig = this.appConfigService.getConfig();
    this.isAuthenticatedUrl = config.bffUrl + '/authenticated';
  }

  public isAuthenticated(): Observable<boolean> {
    const self = this;

    if (typeof this.userState === 'undefined') {

      return new Observable<boolean>(observer => {
        self
          .checkAuthenticated()
          .subscribe(data => {
            self.userState = data;
            observer.next(data.authenticated);
            observer.complete();
          }, (error) => {
            console.warn('isAuthenticated: problem', error);
            observer.next(false);
            observer.complete();
          });
      });
    } else {
      return new Observable<boolean>(observer => {
        observer.next(self.userState.authenticated);
        observer.complete();
      });
    }
  }

  checkAuthenticated(): Observable<UserState> {
    console.log('calling auth service');
    return this.http.get<UserState>(this.isAuthenticatedUrl, {
      withCredentials: true
    });
  }
}
