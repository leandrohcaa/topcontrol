import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {FormControl} from '@angular/forms';

import { AuthenticationRepository } from '../../repository/index';
import { AlertService } from '../../services/index';
import { Usuario } from '../../models/index';

@Component({
    moduleId: module.id,
    templateUrl: 'register.component.html'
})

export class RegisterComponent {
    model: Usuario = <Usuario>{};
    confirmacaoSenha: string;

    constructor(
        private router: Router,
        private alertService: AlertService,
        private authenticationRepository: AuthenticationRepository) { }

    register() {
        if (this.validate(this.model)) {
            var owner = this.authenticationRepository.getOwner();
            this.model.dono = owner.dono;

            this.authenticationRepository.create(this.model)
                .subscribe(
                data => {
                    this.router.navigate(['/login']);
                },
                error => {
                    this.alertService.catchError(error);
                });
        }
    }

    validate(model: Usuario): boolean {
        if (model.senha != this.confirmacaoSenha) {
            this.alertService.error('Confirmação de senha incorreta.');
            return false;
        } else
            if (model.senha.length < 6) {
                this.alertService.error('A senha deve ter no mínimo 6 caracteres.');
                return false;
            }
        return true;
    }
}
