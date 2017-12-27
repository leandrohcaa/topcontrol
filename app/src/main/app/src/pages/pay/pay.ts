import { Component } from '@angular/core';
import {trigger, state, style, animate, transition} from '@angular/animations';
import { NavController, NavParams, ModalController } from 'ionic-angular';

import { ModalDetailPay } from '../../commons/modal/detailPay/index';
import { AlertService, PoolService, CommonsService } from '../../services/index';
import { RequestRepository, AuthenticationRepository } from '../../repository/index';
import { GrupoProdutoProdutoDTO } from '../../models/index';
import * as moment from 'moment';
import * as $ from 'jquery';

@Component({
    selector: 'page-pay',
    templateUrl: 'pay.html',
    animations: [
        trigger('visibleState', [
            state('visible', style({ display: 'flex', opacity: 100 })),
            state('hidden', style({ display: 'none', opacity: 0 })),
            state('hiddenByFilter', style({ display: 'none', opacity: 0 })),
            transition('* => hidden', animate('300ms ease')),
            transition('* => hiddenByFilter', animate(0))
        ]),        
        trigger('visibleWaitConfirmState', [
            state('waitConfirm', style({ display: 'flex', opacity: 100 })),
            state('visible', style({ display: 'none', opacity: 0 })),
            state('hidden', style({ display: 'none', opacity: 0 })),
            state('hiddenByFilter', style({ display: 'none', opacity: 0 })),
            transition('* => waitConfirm', animate('300ms ease'))
        ])
    ]
})
export class PayPage {
    poolFrequency: number = 3000;
    intervalWaitConfirm: number = 3000;
    lastModification: Date;
    paymentList: Array<GrupoProdutoProdutoDTO> = [];

    constructor(public navCtrl: NavController, public navParams: NavParams,
        public requestRepository: RequestRepository, public alertService: AlertService,
        public authenticationRepository: AuthenticationRepository,
        public poolService: PoolService,
        public modalCtrl: ModalController,
        public commonsService: CommonsService) {

    }

    ngOnInit() {
        this.refreshList(false);
        this.poolService.setPool(this.functionPool, this, this.poolFrequency, null);
    }

    refreshList(isToLoading: boolean) {
        let usuario = this.authenticationRepository.getUser();
        if (usuario != null && usuario.dono != null) {
            if (isToLoading)
                this.alertService.showLoading();
            this.requestRepository.getForPaymentList(usuario.dono)
                .subscribe(
                data => {
                    this.paymentList = data.list;                   
                    for (let paymentDTO of this.paymentList) {
                        paymentDTO.estadoExibicao = 'visible';
                    }                    
                    if (data.lastModification != null && data.lastModification != 'Invalid Date')
                        this.lastModification = new Date(data.lastModification[0], data.lastModification[1] - 1, data.lastModification[2], data.lastModification[3], data.lastModification[4], data.lastModification[5]);
                    if (isToLoading)
                        this.alertService.hideLoading();
                },
                error => {
                    this.alertService.catchError(error);
                    if (isToLoading)
                        this.alertService.hideLoading();
                });
        }
    }

    functionPool(thisRef: any) {
        let usuario = thisRef.authenticationRepository.getUser();
        if (usuario != null && usuario.dono != null) {
            thisRef.requestRepository.getLastModificationRequisicaoProdutoByDono(usuario.dono.id)
                .subscribe(
                data => {
                    if (data != null) {
                        var lastModificationAux = thisRef.commonsService.localDateTimeToJSDate(data);
                        if (thisRef.lastModification == null || lastModificationAux.getTime() != thisRef.lastModification.getTime()) {
                            thisRef.refreshList(true);
                            thisRef.lastModification = lastModificationAux;
                        }
                    }
                    thisRef.poolService.setPool(thisRef.functionPool, thisRef, thisRef.poolFrequency, null);
                },
                error => {
                    thisRef.alertService.logError(error);
                    thisRef.poolService.setPool(thisRef.functionPool, thisRef, thisRef.poolFrequency, null);
                });
        }
    }

    itemTapped(event, item: any) {
        if(item.estadoExibicao != 'waitConfirm'){
            let modal = this.modalCtrl.create(ModalDetailPay, { data: item });
            modal.onDidDismiss(data => {
                if (data != null) {
                    var item = data.item;
                    if (data.action == 'P') {
                        this.concludePayment([this, item.requisicaoProdutoId]);
                    } else if (data.action == 'CR') {
                        this.cancelRequest(item.requisicaoProdutoId);
                    }
                }
            });
            modal.present();
        } else {            
            var thisRef = this;
            $.each(thisRef.paymentList, function(index, element) {
                if (element.requisicaoProdutoId == item.requisicaoProdutoId) {
                    element.estadoExibicao = 'visible';
                    thisRef.poolService.clearInterval(item.interval);
                }
            });
        }
    }

    concludePayment(argumentsVar: Array<any>) {
        var thisRef = argumentsVar[0];
        var requisicaoProdutoId = argumentsVar[1];
        $.each(thisRef.paymentList, function(index, element) {
            if (element.requisicaoProdutoId == requisicaoProdutoId) {
                thisRef.requestRepository.concludePayment(element.requisicaoProdutoId)
                    .subscribe(
                    data => {
                        if (data != null && data != 'Invalid Date')
                            thisRef.lastModification = thisRef.commonsService.localDateTimeToJSDate(data);
                        element.estadoExibicao = 'hidden';
                    },
                    error => {
                        thisRef.alertService.catchError(error);
                    });
            }
        });
    }
    
    cancelRequest(requisicaoProdutoId: number) {
        var thisRef = this;
        $.each(thisRef.paymentList, function(index, element) {
            if (element.requisicaoProdutoId == requisicaoProdutoId) {
                thisRef.requestRepository.cancelRequest(element.requisicaoProdutoId)
                    .subscribe(
                    data => {
                        if (data != null && data != 'Invalid Date')
                            thisRef.lastModification = thisRef.commonsService.localDateTimeToJSDate(data);
                        element.estadoExibicao = 'hidden';
                    },
                    error => {
                        thisRef.alertService.catchError(error);
                    });
            }
        });
    }
    
    swipeEvent(event, item: GrupoProdutoProdutoDTO) {
        if(item.estadoExibicao != 'waitConfirm'){
            if (event.deltaX > 0) {
                var thisRef = this;
                $.each(thisRef.paymentList, function(index, element) {
                    if (element.requisicaoProdutoId == item.requisicaoProdutoId) {
                        element.estadoExibicao = 'waitConfirm';
                        element.interval = thisRef.poolService.setInterval(thisRef.concludePayment, [thisRef, element.requisicaoProdutoId], thisRef.intervalWaitConfirm, null);
                    }
                });
            }
        }
    }

    getItems(ev) {
        var val = ev.target.value;
        $.each(this.paymentList, function(index, element) {
            if (element.estadoExibicao != 'hidden') {
                if (element.usuarioRequisicao.toLowerCase().indexOf(val.toLowerCase()) > -1)
                    element.estadoExibicao = 'visible';
                else
                    element.estadoExibicao = 'hiddenByFilter';
            }
        });
    }

    formatLocalDate(datetime: any) {
        return moment(new Date(datetime[0], datetime[1] - 1, datetime[2], datetime[3], datetime[4], datetime[5])).format('DD/MM/YY');
    }
    formatLocalTime(datetime: any) {
        return moment(new Date(datetime[0], datetime[1] - 1, datetime[2], datetime[3], datetime[4], datetime[5])).format('HH:mm:ss');
    }
}
