import { Component, OnInit } from '@angular/core';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';
import { Produto, Requisicao, GrupoProdutoProdutoDTO } from '../../models/index';
import { ProductRepository, RequestRepository } from '../../repository/index';
import { AlertService } from '../../services/index';
import * as $ from 'jquery';

@Component({
    moduleId: module.id,
    templateUrl: 'request.component.html'
})
export class RequestComponent implements OnInit {
	  selectedTabIndex: number;
    productList : Array<GrupoProdutoProdutoDTO>;
    productOrProductGroupList : Array<GrupoProdutoProdutoDTO>;
    selectedProductRequestList : Array<GrupoProdutoProdutoDTO> = [];
    selectedProductPrepareList : Array<GrupoProdutoProdutoDTO> = [];
  
    preferencesPrepareList : Array<GrupoProdutoProdutoDTO>;
    preferencesRequestList : Array<Requisicao>;
    
    productRequestInputFormControl: FormControl;
    productPrepareInputFormControl: FormControl;
    filteredProductRequestList: Observable<any[]>;
    filteredProductPrepareList: Observable<any[]>;
    
    constructor(
        private alertService: AlertService,
        private productRepository: ProductRepository,
        private requestRepository: RequestRepository) {	  
    }   
  
    ngOnInit() {
      this.productRequestInputFormControl = new FormControl();
      this.productPrepareInputFormControl = new FormControl();
                              
      this.preferencesRequestList = [];
      this.preferencesPrepareList = [];  
      this.requestRepository.getPrepareAndRequestList().subscribe(
            data => {
                this.preferencesPrepareList = data.produtoDTOList;
                this.preferencesRequestList = data.requisicaoList;
            },
            error => {
                this.alertService.catchError(error);
            });      
      
      this.productList = [];
      this.productRepository.getProduct().subscribe(
            data => {
                this.productList = JSON.parse(data['_body']);
              
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
                this.productOrProductGroupList = JSON.parse(data['_body']);
              
                this.filteredProductRequestList = this.productRequestInputFormControl.valueChanges
                  .startWith(null)
                  .map(product => this.filterProduct(product, this.productOrProductGroupList));
            },
            error => {
                this.alertService.catchError(error);
            });
    }
  
    filterProduct(productFilter: any, productOrProductGroupList: any) {
      if(productFilter != null){
        var productFilterName = typeof(productFilter) == 'string' ? productFilter : productFilter.nome;
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
      if(selectedProductExisting.length > 0){
        selectedProductRequest = selectedProductExisting[0];
        selectedProductRequest.quantidade++;
      }
      else{
        selectedProductRequest = <GrupoProdutoProdutoDTO>({
                id: evt.option.value.id,
                nome: evt.option.value.nome,
                descricao: evt.option.value.descricao,
                preco: evt.option.value.preco,
                tipo: evt.option.value.tipo})
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
        if (value == 0){
          selectedList.splice(selectedList.findIndex(prod => prod.id == id), 1);
        }else{
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
	
    reservar(): void{
      this.requestRepository.request(this.selectedProductRequestList)
        .subscribe(
                  data => {
                      this.preferencesPrepareList = data.produtoDTOList;
                      this.preferencesRequestList = data.requisicaoList;
                      this.clearReservar();
                      this.selectedTabIndex = 0;
                  },
                  error => {
                      this.alertService.catchError(error);
                  });
    }
    clearReservar(): void{
      this.selectedProductRequestList = [];
    }
    preparar(): void{
      this.requestRepository.prepare(this.selectedProductPrepareList)
        .subscribe(
                  data => {
                      this.preferencesPrepareList = data;
                      this.clearPreparar();
					            this.selectedTabIndex = 0;
                  },
                  error => {
                      this.alertService.catchError(error);
                  });
    }
    clearPreparar(): void{
      this.selectedProductPrepareList = [];
    }
  
    preferencesRequestOnClick($event): void{
      alert();
    }
  
    preparacaoAcessoRapidoTabClick(evt: any) {
        var inputName = evt.currentTarget.id;
        var id = Number(inputName.substring(inputName.indexOf('_') + 1));
        var selectedPreference = this.preferencesPrepareList.filter(prod => prod.id == id)[0];
        selectedPreference.quantidade += 1;
    }
    isPreparacaoAcessoRapidoEditing(): boolean{
        var selectedPreferenceWithQuantidade = this.preferencesPrepareList.filter(prod => prod.quantidade > 0);
        if(selectedPreferenceWithQuantidade.length > 0)
            return true;
        return false;
    }  
    preparacaoAcessoRapidoPrepararClick(): boolean{
        var selectedPreferenceWithQuantidade = this.preferencesPrepareList.filter(prod => prod.quantidade > 0);        
        this.requestRepository.prepare(selectedPreferenceWithQuantidade)
          .subscribe(
                    data => {
                        this.preferencesPrepareList = data;
                        this.preparacaoAcessoRapidoClearClick();
                    },
                    error => {
                        this.alertService.catchError(error);
                    });
    }
    preparacaoAcessoRapidoClearClick(): boolean{
        for (let preference of this.preferencesPrepareList) {
            preference.quantidade = 0;
        }
    }
}