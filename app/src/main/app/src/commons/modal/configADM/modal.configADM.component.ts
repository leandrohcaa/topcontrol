import {Component} from '@angular/core';
import { ViewController } from 'ionic-angular';
import { WebServiceService } from '../../../services/index';

@Component({
    selector: 'modal.configADM.component',
    templateUrl: 'modal.configADM.component.html',
})
export class ModalConfigADM {
    host: string;    
    
    constructor(public viewCtrl: ViewController, public webServiceService: WebServiceService) {
        this.host = this.webServiceService.getURL();
    }

    dismiss() {
        this.viewCtrl.dismiss();
    }

    changeHost() {
        this.webServiceService.setURL(this.host);
        this.dismiss();
    }
}