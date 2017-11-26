import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService, AlertService } from '../../auth/_services/index';

@Component({
    moduleId: module.id,
    templateUrl: 'login.component.html'
})
export class LoginComponent implements OnInit {
    returnUrl: string;
    usuario: string;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private alertService: AlertService) { }

    ngOnInit() {
        // reset login status
        this.authenticationService.logout();

        // get return url from route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }
	
	login(): void{    
        localStorage.setItem('currentUser', 'leandro.hcaa');
        this.router.navigate([this.returnUrl]);
        return;//FIXME
        this.authenticationService.login('', '')
            .subscribe(
                data => {
                    this.router.navigate([this.returnUrl]);
                },
                error => {
                    alert(error);
                    this.alertService.error(error);
                });
	}
}
