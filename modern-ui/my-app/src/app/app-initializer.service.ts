import {Injectable} from '@angular/core';
import {AppConfigService} from './app.config.service';

@Injectable()
export class AppInitializerService {

  constructor(private appConfigService: AppConfigService) {
  }


  private loadAppConfig(): Promise<Object> {
    return new Promise((resolve, reject) => {
      this.appConfigService.load().then(() => {
        resolve(true);
      }).catch((e) => {
        reject(e);
      });
    });
  }


  /**
   * This method loads "./app-config" to get the current URL configurations
   */
  public load(): Promise<Object> {
    return new Promise((resolve, reject) => {
      this.loadAppConfig()
        .then(() => {
          resolve(true);
        });
    });
  }
}
