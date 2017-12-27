import { Component, OnInit } from '@angular/core';
import { GrupoProdutoProdutoDTO } from '../../models/index';
import { ProductRepository } from '../../repository/index';
import { AlertService, CommonsService } from '../../services/index';
import {MatTableDataSource} from '@angular/material';

@Component({
    moduleId: module.id,
    templateUrl: 'dataConfig.component.html'
})
export class DataConfigComponent implements OnInit {
    selectedTabIndex: number;

    productDisplayedColumns = ['id', 'nome', 'descricao', 'uploadImage'];
    productDataSource;

    constructor(
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
        alert(row);
    }
}