import {Injectable} from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import {throwError, } from 'rxjs';  // Updated for Angular 6/RxJS 6
import { map, catchError } from 'rxjs/operators';
import {environment} from '../environments/environment';

export interface AppConfig {
    bffUrl: string;
}

@Injectable()
export class AppConfigService {

  private config: AppConfig = null;


  constructor(private http: HttpClient) {
  }

  /**
   * Use to get the data found in the config file
   */
  public getConfig(): AppConfig {
        return this.config;
  }

  /**
   * This method loads "./app-config" to get the current DRSS configurations
   */
  public load(): Promise<Object> {
    const self = this;
    return new Promise((resolve, reject) => {
      let request: any = null;
      request = self.http.get<AppConfig>(environment.appConfigURL);

      if (request) {
        request.subscribe(
            (data: AppConfig) => {
                self.config = data;
                resolve(true);
            },
            (error) => {
                if (error.error instanceof ErrorEvent) {
                    // A client-side or network error occurred. Handle it accordingly.
                    console.error('An error occurred:', error.error.message);
                } else {
                    // The backend returned an unsuccessful response code.
                    // The response body may contain clues as to what went wrong,
                    console.error(
                      `Backend returned code ${error.status}, ` +
                      `body was: ${error.error}`);
                }

                resolve(error);

                return throwError(error.error || 'Server error');
            }
        );
      } else {
        console.error('Config file ' + environment.appConfigURL + ' is not valid');
        resolve(true);
      }
    });
  }
}
