import {Component, Inject} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

@Component({
  selector: 'modal.alert.component',
  templateUrl: 'modal.alert.component.html',
})
export class ModalAlert {
    
  constructor(
    public dialogRef: MatDialogRef<ModalAlert>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  close(): void {
    this.dialogRef.close();
  }
  
}