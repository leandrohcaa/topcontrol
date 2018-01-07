import {Component, Inject} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationRepository } from '../../../repository/index';
import { AlertService } from '../../../services/index';
import { Usuario } from '../../../models/index';

@Component({
    selector: 'modal.owner.component',
    templateUrl: 'modal.owner.component.html',
})
export class ModalOwner {

    dataModal: any;
    model: Usuario = <Usuario>{};

    constructor(
        public dialogRef: MatDialogRef<ModalOwner>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private route: ActivatedRoute,
        private router: Router,
        private authenticationRepository: AuthenticationRepository,
        private alertService: AlertService) {
        this.dataModal = data;
        if (this.dataModal.action == 'logout') 
            this.model.usuario = this.authenticationRepository.getOwner().usuario;
    }

    execute(): void {
        if (this.dataModal.action == 'login') {
            this.loginOwner();
        } else {
            this.logoutOwner();
        }
    }

    loginOwner(): void {
        this.authenticationRepository.loginOwner(this.model)
            .subscribe(
            data => {
                this.close();
            },
            error => {
                this.alertService.catchError(error);
            });
    }

    logoutOwner(): void {
        this.authenticationRepository.logoutOwner(this.model)
            .subscribe(
            data => {
                this.close();
            },
            error => {
                this.alertService.catchError(error);
            });
    }

    close(): void {
        this.dialogRef.close();
    }

}