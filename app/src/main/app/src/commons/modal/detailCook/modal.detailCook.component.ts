import {Component} from '@angular/core';
import { NavParams, ViewController } from 'ionic-angular';
import { GrupoProdutoProdutoDTO } from '../../../models/index';
import { CommonsService } from '../../../services/index';
import * as moment from 'moment';

@Component({
    selector: 'modal.detailCook.component',
    templateUrl: 'modal.detailCook.component.html',
})
export class ModalDetailCook {
    data: GrupoProdutoProdutoDTO;
    constructor(public params: NavParams, public viewCtrl: ViewController,
        public commonsService: CommonsService) {
        this.data = params.get('data');
    }

    dismiss() {
        this.viewCtrl.dismiss();
    }

    formatLocalDateTime(datetime: any) {
        return moment(this.commonsService.localDateTimeToJSDate(datetime)).format('DD/MM/YY HH:mm:ss');
    }

    prepare() {
        this.viewCtrl.dismiss({ action: 'P', item: this.data });
    }

    cancelPrepare() {
        this.viewCtrl.dismiss({ action: 'CP', item: this.data });
    }

    cancelRequest() {
        this.viewCtrl.dismiss({ action: 'CR', item: this.data });
    }
}