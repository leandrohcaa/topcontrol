import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationRepository } from '../../repository/index';
import { AlertService } from '../../services/index';
import { FormControl } from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';

import { Usuario } from '../../models/index';

@Component({
    moduleId: module.id,
    templateUrl: 'login.component.html'
})
export class LoginComponent implements OnInit {
    returnUrl: string;
    model: Usuario = <Usuario>{};
    usuarioInputFormControl: FormControl;
    filteredUsuarioList: Observable<any[]>;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authenticationRepository: AuthenticationRepository,
        private alertService: AlertService) { }

    ngOnInit() {
        // reset login status
        this.authenticationRepository.logout();

        // get return url from route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

        this.usuarioInputFormControl = new FormControl();
        this.filteredUsuarioList = this.usuarioInputFormControl.valueChanges
            .startWith(null)
            .map(user => this.filterUser(user));
    }


    filterUser(userTyped: any) {
        if (userTyped != null) {
            var usuarioFilterName = typeof (userTyped) == 'string' ? userTyped : userTyped.usuario;
            return this.getUsuarioList().filter(user =>
                user.usuario.toLowerCase().indexOf(usuarioFilterName.toLowerCase()) >= 0);
        }
        return this.getUsuarioList();
    }
    usuarioOnSelect(evt: any) {
        var usuarioSelected = evt.option.value;
        this.model.usuario = usuarioSelected.usuario;
        if (!this.isToShowPassword())
            this.login();
    }

    login(): void {
        if (!this.isToShowPassword()) {
            this.model.senha = null;
        }
        this.authenticationRepository.login(this.model)
            .subscribe(
            data => {
                this.router.navigate([this.returnUrl]);
            },
            error => {
                this.alertService.catchError(error);
            });
    }

    isToShowPassword(): boolean {
        var owner = this.authenticationRepository.getOwner();
        if (owner != null && owner.dono != null && !owner.dono.utilizaSenha) {
            return false;
        }
        return true;
    }

    getUsuarioList(): Array<Usuario> {
        var owner = this.authenticationRepository.getOwner();
        if (owner != null && owner.dono.clienteList != null && owner.dono.clienteList.length > 0) {
            return owner.dono.clienteList;
        }
        return [];
    }
    
    isToShowRegistrar(): boolean {
        var owner = this.authenticationRepository.getOwner();
        if (owner != null && owner.dono != null) {
            return true;
        }
        return false;
    }

    usuarioWithDisplay(usuario: any): string {
        if (usuario != null)
            return typeof (usuario) == 'string' ? usuario : usuario.usuario;
        return null;
    }
}
