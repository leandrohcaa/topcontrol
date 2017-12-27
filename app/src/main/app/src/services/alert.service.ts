import { Injectable } from '@angular/core';
import { ModalController, LoadingController, Loading } from 'ionic-angular';
import { ModalAlert } from '../commons/modal/alert/index';
import { CommonsService }  from './commons.service';

@Injectable()
export class AlertService {

    loading: Loading;

    constructor(public modalCtrl: ModalController, public loadingCtrl: LoadingController,
        public commonsService: CommonsService) {
    }

    catchError(error: any) {
        this.error(this.commonsService.isJsonString(error._body) ? JSON.parse(error._body).message : 'Erro de conectividade com o servidor.');
    }

    logError(error: any) {
        console.log(this.commonsService.isJsonString(error._body) ? JSON.parse(error._body).message : 'Erro de conectividade com o servidor.');
    }
    
    error(message: string) {
        let modal = this.modalCtrl.create(ModalAlert, { data: { mode: 'error', text: message } });
        modal.present();
    }

    showLoading(): void {
        this.loading = this.loadingCtrl.create({
            content: 'Carregando'
        });
        this.loading.present();
    }

    hideLoading(): void {
        this.loading.dismiss();
    }
}