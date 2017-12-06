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
        if(this.validate(this.model)){
          
          var owner = this.authenticationRepository.getOwner();
          if(owner != null && owner.usuarioNegocioList != null && owner.usuarioNegocioList.length > 0)
              this.model.dono = owner.usuarioNegocioList[0];
          
		      this.authenticationRepository.create(this.model)
              .subscribe(
                  data => {
                      this.alertService.success('Registration successful', true);
                      this.router.navigate(['/login']);

                      var owner = this.authenticationRepository.getOwner();
                      owner.senha = null;
                      this.authenticationRepository.loginOwner(owner).subscribe(
                          data => { },
                          error => { this.alertService.catchError(error); });
                  },
                  error => {
                      this.alertService.catchError(error);
                  });
        }
    }
  
    validate(model: Usuario): boolean{
        if(model.senha != this.confirmacaoSenha){
          this.alertService.error('Confirmação de senha incorreta.');
          return false;
        }else 
        if(model.senha.length < 6){
          this.alertService.error('A senha deve ter no mínimo 6 caracteres.');
          return false;
        }
        return true;
    }
}
