import { Component, OnInit } from '@angular/core';
import { AuthService } from './auth.service';

@Component({
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  authenticated: boolean;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {

    this.authService.isAuthenticated().subscribe((res:boolean)=>{
      this.authenticated = res;
    });
  }  
}
