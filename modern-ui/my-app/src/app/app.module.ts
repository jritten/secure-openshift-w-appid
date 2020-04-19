import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home.component';
import { DetailsComponent } from './details.component';
import { AppComponent } from './app.component';
import { Injectable } from '@angular/core';
import { XhrInterceptor } from './xhr.interceptor.service';

import {AppConfigService} from './app.config.service';
import {AppInitializerService} from './app-initializer.service';
import {AuthService} from './auth.service';

import { AuthGuard } from './shared';


export function init_app(appInitializerService: AppInitializerService) {
  return () => appInitializerService.load();
}

const routes: Routes = [
  { path: 'details', component: DetailsComponent, canActivate: [AuthGuard]},
  { path: '', pathMatch: 'full', redirectTo: 'home'},
  { path: '**', redirectTo: 'home'},
  { path: 'home', component: HomeComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    DetailsComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    AuthGuard,
    AppConfigService,
    AuthService,
    AppInitializerService,
    {
      provide: APP_INITIALIZER,
      useFactory: init_app,
      deps: [AppInitializerService],
      multi: true
    },
    { provide: HTTP_INTERCEPTORS,
      useClass: XhrInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
