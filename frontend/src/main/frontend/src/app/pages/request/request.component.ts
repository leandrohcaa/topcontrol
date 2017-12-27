import { Component, OnInit } from '@angular/core';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';
import { Produto, Requisicao, GrupoProdutoProdutoDTO, RequisicaoDTO } from '../../models/index';
import { ProductRepository, RequestRepository, AuthenticationRepository } from '../../repository/index';
import { AlertService, PoolService, CommonsService } from '../../services/index';
import { ModalCaracteristicaPrepare } from '../../commons/modal/caracteristicaPrepare/index';
import * as moment from 'moment';
import * as $ from 'jquery';
import { MatDialog } from '@angular/material';

@Component({
    moduleId: module.id,
    templateUrl: 'request.component.html'
})
export class RequestComponent implements OnInit {
    selectedTabIndex: number;
    lastModification: Date;

    productList: Array<GrupoProdutoProdutoDTO>;
    productOrProductGroupList: Array<GrupoProdutoProdutoDTO>;
    selectedProductRequestList: Array<GrupoProdutoProdutoDTO> = [];
    selectedProductPrepareList: Array<GrupoProdutoProdutoDTO> = [];

    preferencesPrepareList: Array<GrupoProdutoProdutoDTO>;
    preferencesRequestList: Array<RequisicaoDTO>;

    productRequestInputFormControl: FormControl;
    productPrepareInputFormControl: FormControl;
    filteredProductRequestList: Observable<any[]>;
    filteredProductPrepareList: Observable<any[]>;

    constructor(
        private dialog: MatDialog,
        private alertService: AlertService,
        private productRepository: ProductRepository,
        private requestRepository: RequestRepository,
        private poolService: PoolService,
        private authenticationRepository: AuthenticationRepository,
        private commonsService: CommonsService) {
    }

    ngOnInit() {
        this.productRequestInputFormControl = new FormControl();
        this.productPrepareInputFormControl = new FormControl();

        this.preferencesRequestList = [];
        this.preferencesPrepareList = [];
        this.requestRepository.getPrepareAndRequestList().subscribe(
            data => {
                this.preferencesPrepareList = data.preparacaoList;
                this.preferencesRequestList = data.requisicaoList;
                if (data.lastModification != null)
                    this.lastModification = this.commonsService.localDateTimeToJSDate(data.lastModification);
            },
            error => {
                this.alertService.catchError(error);
            });

        this.productList = [];
        this.productRepository.getProduct().subscribe(
            data => {
                this.productList = data;

                this.filteredProductPrepareList = this.productPrepareInputFormControl.valueChanges
                    .startWith(null)
                    .map(product => product ? this.filterProduct(product, this.productList) : this.productList.slice());
            },
            error => {
                this.alertService.catchError(error);
            });
        this.productOrProductGroupList = [];
        this.productRepository.getProductAndGroup().subscribe(
            data => {
                this.productOrProductGroupList = data;

                this.filteredProductRequestList = this.productRequestInputFormControl.valueChanges
                    .startWith(null)
                    .map(product => this.filterProduct(product, this.productOrProductGroupList));
            },
            error => {
                this.alertService.catchError(error);
            });

        this.poolService.setPool(this.functionPool, this, 3000, null);
    }

    functionPool(thisRef: any) {
        let usuario = thisRef.authenticationRepository.getUser();
        if (usuario != null) {
            thisRef.requestRepository.getLastModificationRequisicaoProdutoByUser(usuario.id)
                .subscribe(
                data => {
                    if (data != null) {
                        var lastModificationAux = thisRef.commonsService.localDateTimeToJSDate(data);
                        if (thisRef.lastModification == null || lastModificationAux.getTime() != thisRef.lastModification.getTime()) {
                            thisRef.refreshPrepareList();
                            thisRef.lastModification = lastModificationAux;
                        }
                    }
                    thisRef.poolService.setPool(thisRef.functionPool, thisRef, 3000, null);
                },
                error => {
                    thisRef.alertService.logError(error);
                    thisRef.poolService.setPool(thisRef.functionPool, thisRef, 3000, null);
                });
        }
    }

    refreshPrepareList() {
        this.alertService.showLoading();
        this.requestRepository.getPrepareList().subscribe(
            data => {
                this.preferencesPrepareList = data;
                this.alertService.hideLoading();
            },
            error => {
                this.alertService.catchError(error);
                this.alertService.hideLoading();
            });
    }

    filterProduct(productFilter: any, productOrProductGroupList: any) {
        if (productFilter != null) {
            var productFilterName = typeof (productFilter) == 'string' ? productFilter : productFilter.nome;
            return productOrProductGroupList.filter(product =>
                product.nome.toLowerCase().indexOf(productFilterName.toLowerCase()) >= 0);
        }
        return productOrProductGroupList;
    }
    productRequestOnSelect(evt: any) {
        this.productOnSelect(evt, this.selectedProductRequestList, this.productRequestInputFormControl);
    }
    productPrepareOnSelect(evt: any) {
        this.productOnSelect(evt, this.selectedProductPrepareList, this.productPrepareInputFormControl);
    }
    productOnSelect(evt: any, selectedList: any, formControl: FormControl) {
        var selectedProductRequest;
        var selectedProductExisting = selectedList.filter(prod => prod.id == evt.option.value.id);
        if (selectedProductExisting.length > 0) {
            selectedProductRequest = selectedProductExisting[0];
            selectedProductRequest.quantidade++;
        }
        else {
            selectedProductRequest = evt.option.value;
            selectedProductRequest.quantidade = 1;
            selectedList.push(selectedProductRequest);
        }

        formControl.setValue(null);
        $('#PrepararInputId,#ReservarInputId').blur();
    }
    productWithDisplay(product: any): string {
        return product ? product.nome : '';
    }

    inputQuantitySelectedProductRequestOnChange(evt: any) {
        this.inputQuantitySelectedOnChange(evt, this.selectedProductRequestList);
    }
    inputQuantitySelectedProductPrepareOnChange(evt: any) {
        this.inputQuantitySelectedOnChange(evt, this.selectedProductPrepareList);
    }
    inputQuantitySelectedOnChange(evt: any, selectedList: any) {
        var inputName = evt.currentTarget.name;
        var value = Number(evt.currentTarget.value);
        var id = Number(inputName.substring(inputName.indexOf('_') + 1));
        if (value == 0) {
            selectedList.splice(selectedList.findIndex(prod => prod.id == id), 1);
        } else {
            var selectedProductExisting = selectedList.filter(prod => prod.id == id)[0];
            selectedProductExisting.quantidade = value;
        }
    }

    isReservarButtonVisible(): boolean {
        return this.selectedProductRequestList.length == 0 ? false : true;
    }
    isPrepararButtonVisible(): boolean {
        return this.selectedProductPrepareList.length == 0 ? false : true;
    }

    reservar(): void {
        this.requestRepository.request(this.selectedProductRequestList)
            .subscribe(
            data => {
                this.preferencesPrepareList = data.preparacaoList;
                this.preferencesRequestList = data.requisicaoList;
                if (data.lastModification != null && data.lastModification != 'Invalid Date')
                    this.lastModification = this.commonsService.localDateTimeToJSDate(data.lastModification);
                this.clearReservar();
                this.selectedTabIndex = 0;
            },
            error => {
                this.alertService.catchError(error);
            });
    }
    clearReservar(): void {
        this.selectedProductRequestList = [];
    }
    preparar(): void {
        this.verifyAndPrepareExecute(this.selectedProductPrepareList);
    }
    clearPreparar(): void {
        this.selectedProductPrepareList = [];
    }

    verifyAndPrepareExecute(list: Array<GrupoProdutoProdutoDTO>): void {
        if (list.filter(s => s.grupoCaracteristicaProdutoDTOList.length > 0).length > 0) {
            let dialogRef = this.dialog.open(ModalCaracteristicaPrepare, {
                data: list
            });
            dialogRef.afterClosed().subscribe(result => {
                if (result != null)
                    this.prepareExecute(result);
            });
        } else {
            this.prepareExecute(list);
        }
    }
    
    prepareExecute(list: Array<GrupoProdutoProdutoDTO>): void {
        this.requestRepository.prepare(list)
            .subscribe(
            data => {
                this.preferencesPrepareList = data.preparacaoList;
                if (data.lastModification != null && data.lastModification != 'Invalid Date')
                    this.lastModification = this.commonsService.localDateTimeToJSDate(data.lastModification);
                this.clearPreparar();
                this.selectedTabIndex = 0;
            },
            error => {
                this.alertService.catchError(error);
            });
    }

    requisicaoAcessoRapidoTabClick($event, requisicao: RequisicaoDTO): void {
        this.clearReservar();
        for (let selectedProductRequest of requisicao.grupoProdutoProdutoDTOList) {
            if (this.selectedProductRequestList.filter(s => s.id == selectedProductRequest.id).length == 0) {
                selectedProductRequest.quantidade = 1;
                this.selectedProductRequestList.push(selectedProductRequest);
            } else {
                var selectedProductRequestExisting = this.selectedProductRequestList.filter(s => s.id == selectedProductRequest.id)[0];
                selectedProductRequestExisting.quantidade += 1;
            }
        }
        this.selectedTabIndex = 2;
    }

    preparacaoAcessoRapidoTabClick(evt: any) {
        var inputName = evt.currentTarget.id;
        var id = Number(inputName.substring(inputName.indexOf('_') + 1));
        var selectedPreference = this.preferencesPrepareList.filter(prod => prod.id == id)[0];
        selectedPreference.quantidade += 1;
    }
    isPreparacaoAcessoRapidoEditing(): boolean {
        var selectedPreferenceWithQuantidade = this.preferencesPrepareList.filter(prod => prod.quantidade > 0);
        if (selectedPreferenceWithQuantidade.length > 0)
            return true;
        return false;
    }
    preparacaoAcessoRapidoPrepararClick() {
        this.verifyAndPrepareExecute(this.preferencesPrepareList.filter(prod => prod.quantidade > 0));
    }
    preparacaoAcessoRapidoClearClick() {
        for (let preference of this.preferencesPrepareList) {
            preference.quantidade = 0;
        }
    }

    formatLocalDateTime(datetime: any) {
        return moment(this.commonsService.localDateTimeToJSDate(datetime)).format('DD/MM/YY HH:mm:ss');
    }
}