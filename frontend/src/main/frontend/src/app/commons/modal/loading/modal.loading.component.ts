import {Component, Inject} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material';

@Component({
  selector: 'modal.loading.component',
  templateUrl: 'modal.loading.component.html',
})
export class ModalLoading {
    
  constructor(
    public dialogRef: MatDialogRef<ModalLoading>) { }

  public close(): void {
    this.dialogRef.close();
  }
  
}