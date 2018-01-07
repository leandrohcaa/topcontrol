import {Component, Inject} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import { AlertService } from '../../../services/index';
import {FormControl} from '@angular/forms';

@Component({
    selector: 'modal.dataConfigUploadImage.component',
    templateUrl: 'modal.dataConfigUploadImage.component.html',
})
export class ModalDataConfigUploadImage {
    image: any;
    
    constructor(
        public alertService: AlertService,
        public dialogRef: MatDialogRef<ModalDataConfigUploadImage>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    onUploadFinished(event: any) {
        this.image = event.src;
    }

    confirm(): void {
        if(this.image == null)
            this.alertService.error('Imagem não selecionada.');
        this.dialogRef.close(this.image);
    }

    close(): void {
        this.dialogRef.close();
    }
}