import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationRepository } from './repository/index';
import { MatDialog } from '@angular/material';
import { ModalOwner } from './commons/modal/owner/index';

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
        private authenticationRepository: AuthenticationRepository,
        public dialog: MatDialog) {
    }

    getTheme(): string {
        var owner = this.authenticationRepository.getOwner();
        if (owner != null && owner.dono != null && owner.dono.negocio.theme != null) {
            return owner.dono.negocio.theme;
        }
        return 'default-theme';
    }

    logout(): void {
        this.authenticationRepository.logout();
        this.router.navigate(['/login']);
    }

    logoutOwner(): void {
        this.dialog.open(ModalOwner, {
            data: { action: 'logout' }
        });
    }

    getCurrentUser(): string {
        var currentUser = localStorage.getItem('currentUser');
        return currentUser != null ? JSON.parse(currentUser).nome : '';
    }

    getCurrentOwnerUser(): string {
        var currentOwnerUser = localStorage.getItem('currentOwnerUser');
        return currentOwnerUser != null ? JSON.parse(currentOwnerUser).nome : '';
    }

    getHeaderStyle(page: string): any {
        return this.router.url == '/' + page ?
            {
                '-webkit-box-shadow': 'inset 0px 0px 40px -7px rgba(0,0,0,0.75)',
                '-moz-box-shadow': 'inset 0px 0px 40px -7px rgba(0,0,0,0.75)',
                'box-shadow': 'inset 0px 0px 40px -7px rgba(0,0,0,0.75)'
            } : {};
    }

    navigate(page: string): void {
        this.router.navigate(['/' + page]);
    }

    showOwnerModal() {
        this.dialog.open(ModalOwner, {
            data: { action: 'login' }
        });
    }
    
    isToShowDadosMestre(): boolean {
        var user = this.authenticationRepository.getUser();
        var owner = this.authenticationRepository.getOwner();
        if (owner != null && user.id == owner.id) {
            return true;
        }
        return false;
    }
}