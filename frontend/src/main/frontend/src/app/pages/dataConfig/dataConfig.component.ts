import { Component, OnInit } from '@angular/core';
import { GrupoProdutoProdutoDTO } from '../../models/index';
import { ProductRepository } from '../../repository/index';
import { AlertService, CommonsService } from '../../services/index';
import {MatTableDataSource} from '@angular/material';
import { ModalDataConfigUploadImage } from '../../commons/modal/dataConfigUploadImage/index';
import { MatDialog } from '@angular/material';

@Component({
    moduleId: module.id,
    templateUrl: 'dataConfig.component.html'
})
export class DataConfigComponent implements OnInit {
    selectedTabIndex: number;

    productDisplayedColumns = ['id', 'nome', 'descricao', 'uploadImage'];
    productDataSource;

    constructor(
        private dialog: MatDialog,
        private alertService: AlertService,
        private productRepository: ProductRepository,
        private commonsService: CommonsService) {
    }

    ngOnInit() {
        this.productDataSource = new MatTableDataSource([]);
        this.productRepository.getProduct().subscribe(
            data => {
                this.productDataSource = new MatTableDataSource(data);
            },
            error => {
                this.alertService.catchError(error);
            });
    }

    productUploadImage(row: GrupoProdutoProdutoDTO) {
        let dialogRef = this.dialog.open(ModalDataConfigUploadImage);
        dialogRef.afterClosed().subscribe(result => {
            if (result != null) {
                this.alertService.showLoading();
                this.productRepository.saveImage(row, result).subscribe(
                    data => {
                        this.alertService.hideLoading();
                    },
                    error => {
                        this.alertService.hideLoading();
                        this.alertService.catchError(error);
                    });
            }
        });
    }
}