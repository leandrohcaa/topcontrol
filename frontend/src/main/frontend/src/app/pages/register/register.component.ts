import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {FormControl} from '@angular/forms';

import { AlertService, UserService } from '../../auth/_services/index';
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
        private userService: UserService,
        private alertService: AlertService) { }

    register() {      
        if(this.model.senha != this.confirmacaoSenha){
          this.alertService.error('Confirmação de senha incorreta.');
        }else{
          this.model.dono = false;
          this.userService.create(this.model)
              .subscribe(
                  data => {
                      this.alertService.success('Registration successful', true);
                      this.router.navigate(['/login']);
                  },
                  error => {
                      this.alertService.error(error);
                  });
        }
    }
}
