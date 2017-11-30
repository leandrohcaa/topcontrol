import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from './auth/_services/index';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {  
    title = 'app';

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService) { 
     }
  
    logout(): void{
        this.authenticationService.logout();
        this.router.navigate(['/login']);
    }
  
    getCurrentUser(): string {
        return localStorage.getItem('currentUser');
    }
  
    getHeaderStyle(page: string): any {
        return this.router.url == '/' + page ?  { '-webkit-box-shadow': 'inset 0px 0px 25px -7px rgba(0,0,0,0.75)', '-moz-box-shadow': 'inset 0px 0px 25px -7px rgba(0,0,0,0.75)', 'box-shadow': 'inset 0px 0px 25px -7px rgba(0,0,0,0.75)' } : {};
    }
  
    navigate(page: string): void {
        this.router.navigate(['/' + page]);
    }
    
}