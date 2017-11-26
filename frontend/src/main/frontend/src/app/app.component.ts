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
    
}