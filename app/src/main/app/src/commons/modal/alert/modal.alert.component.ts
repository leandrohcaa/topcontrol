import {Component} from '@angular/core';
import { NavParams, ViewController } from 'ionic-angular';

@Component({
    selector: 'modal.alert.component',
    templateUrl: 'modal.alert.component.html',
})
export class ModalAlert {
    data: any;
    constructor(public params: NavParams, public viewCtrl: ViewController) {
        this.data = params.get('data');
    }

    dismiss() {
        this.viewCtrl.dismiss();
    }
}