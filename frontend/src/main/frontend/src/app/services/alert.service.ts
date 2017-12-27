import { Injectable } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';
import { MatDialog } from '@angular/material';
import { ModalAlert } from './../commons/modal/alert/index';
import { ModalLoading } from './../commons/modal/loading/index';
import { CommonsService }  from './commons.service';

@Injectable()
export class AlertService {
    subject = new Subject<any>();
    keepAfterNavigationChange = false;

    constructor(private router: Router,
        public dialog: MatDialog,
        public commonsService: CommonsService) {
        // clear alert message on route change
        router.events.subscribe(event => {
            if (event instanceof NavigationStart) {
                if (this.keepAfterNavigationChange) {
                    // only keep for a single location change
                    this.keepAfterNavigationChange = false;
                } else {
                    // clear alert
                    this.subject.next();
                }
            }
        });
    }

    success(message: string, keepAfterNavigationChange = false) {
        this.keepAfterNavigationChange = keepAfterNavigationChange;
        this.subject.next({ type: 'success', text: message });
    }

    catchError(error: any) {
        this.error(this.commonsService.isJsonString(error._body) ? JSON.parse(error._body).message : 'Erro de conectividade com o servidor.');
    }
    
    logError(error: any) {
        console.log(this.commonsService.isJsonString(error._body) ? JSON.parse(error._body).message : 'Erro de conectividade com o servidor.');
    }

    error(message: string, keepAfterNavigationChange = false) {
        this.dialog.open(ModalAlert, {
            data: { mode: 'error', text: message }
        });
    }

    getMessage(): Observable<any> {
        return this.subject.asObservable();
    }

    showLoading() {
        this.dialog.open(ModalLoading, {disableClose: true});
    }

    hideLoading() {
        this.dialog.closeAll();
    }
}