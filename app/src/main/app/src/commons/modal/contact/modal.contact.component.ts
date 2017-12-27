import {Component} from '@angular/core';
import { ViewController } from 'ionic-angular';

@Component({
    selector: 'modal.contact.component',
    templateUrl: 'modal.contact.component.html',
})
export class ModalContact {
    constructor(public viewCtrl: ViewController) {
    }

    dismiss() {
        this.viewCtrl.dismiss();
    }
}