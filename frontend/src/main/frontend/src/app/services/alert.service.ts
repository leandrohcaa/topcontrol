import { Injectable } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';
import { MatDialog } from '@angular/material';
import { ModalAlert } from './../commons/modal/alert/index';

@Injectable()
export class AlertService {
    private subject = new Subject<any>();
    private keepAfterNavigationChange = false;

    constructor(private router: Router,
                  public dialog: MatDialog) {
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
        this.error(typeof(error._body) == 'string' ? JSON.parse(error._body).message : 'Erro de conectividade com o servidor.');
    }

    error(message: string, keepAfterNavigationChange = false) {
        this.dialog.open(ModalAlert, {
              data: { mode: 'error', text: message }
            });
    }

    getMessage(): Observable<any> {
        return this.subject.asObservable();
    }
}