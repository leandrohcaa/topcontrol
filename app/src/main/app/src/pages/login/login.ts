import { Component } from '@angular/core';
import { NavController, IonicPage, ModalController } from 'ionic-angular';
import { Usuario } from '../../models/index';
import { AuthenticationRepository } from '../../repository/index';
import { AlertService } from '../../services/index';
import { CookPage } from '../../pages/cook/cook';
import { ModalConfigADM } from '../../commons/modal/configADM/index';

@IonicPage()
@Component({
    selector: 'page-login',
    templateUrl: 'login.html',
})
export class LoginPage {
    model: Usuario = <Usuario>{};

    constructor(public modalCtrl: ModalController,
        private nav: NavController, private authenticationRepository: AuthenticationRepository,
        private alertService: AlertService) {
    }

    public login() {
        if (this.model != null && this.model.usuario != null && this.model.usuario.toLowerCase() == 'admin' 
                && this.model.senha != null && this.model.senha.toLowerCase() == 'admin') {
            let modal = this.modalCtrl.create(ModalConfigADM);
            modal.present();
        } else {
            this.authenticationRepository.login(this.model)
                .subscribe(
                data => {
                    this.nav.setRoot(CookPage);
                },
                error => {
                    this.alertService.catchError(error);
                });
        }
    }
}