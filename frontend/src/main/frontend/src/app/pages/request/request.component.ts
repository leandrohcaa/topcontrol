import { Component, OnInit } from '@angular/core';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';
import { Produto, Requisicao, ProdutoQuantidadeDTO } from '../../models/index';
import { ProductRepository, RequestRepository } from '../../repository/index';
import { AlertService } from '../../services/index';

@Component({
    moduleId: module.id,
    templateUrl: 'request.component.html'
})
export class RequestComponent implements OnInit {
    productList : Array<Produto>;
    preferencesRequestList : Array<Requisicao>;
    preferencesPrepareList : Array<ProdutoQuantidadeDTO>;
    selectedProductRequestList : Array<ProdutoQuantidadeDTO> = [];
    selectedProductPrepareList : Array<ProdutoQuantidadeDTO> = [];
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
      this.filteredProductRequestList = this.productRequestInputFormControl.valueChanges
        .startWith(null)
        .map(product => product ? this.filterProduct(product) : this.productList.slice());
      
      this.productPrepareInputFormControl = new FormControl();
      this.filteredProductPrepareList = this.productPrepareInputFormControl.valueChanges
        .startWith(null)
        .map(product => product ? this.filterProduct(product) : this.productList.slice());
		  
      this.productList = [];
      this.productRepository.getHierarchyList().subscribe(
            data => {
                this.productList = JSON.parse(data['_body']);
            },
            error => {
                this.alertService.catchError(error);
            });
      this.preferencesRequestList = [];
      this.preferencesPrepareList = [];   
    }
  
    filterProduct(productFilter: any) {
      var productFilterName = typeof(productFilter) == 'string' ? productFilter : productFilter.nome;
      return this.productList.filter(product =>
        product.nome.toLowerCase().indexOf(productFilterName.toLowerCase()) >= 0);
    }  
    productRequestOnSelect(evt: any) {
      var selectedProductRequest;
      var selectedProductExisting = this.selectedProductRequestList.filter(prod => prod.produto.id == evt.option.value.id);
      if(selectedProductExisting.length > 0){
        selectedProductRequest = selectedProductExisting[0];
        selectedProductRequest.quantidade++;
      }
      else{
		    selectedProductRequest = <ProdutoQuantidadeDTO>({produto: evt.option.value})
        selectedProductRequest.quantidade = 1;
        this.selectedProductRequestList.push(selectedProductRequest);
      }
      
      this.productRequestInputFormControl.setValue(null);
    }
    productPrepareOnSelect(evt: any) {
      var selectedProductRequest;
      var selectedProductExisting = this.selectedProductPrepareList.filter(prod => prod.produto.id == evt.option.value.id);
      if(selectedProductExisting.length > 0){
        selectedProductRequest = selectedProductExisting[0];
        selectedProductRequest.quantidade++;
      }
      else{
		    selectedProductRequest = <ProdutoQuantidadeDTO>({produto: evt.option.value})
        selectedProductRequest.quantidade = 1;
        this.selectedProductPrepareList.push(selectedProductRequest);
      }
      
      this.productPrepareInputFormControl.setValue(null);
    }
    productWithDisplay(product: any): string {
      return product ? product.nome : '';
    }
	
    inputQuantitySelectedProductRequestOnChange(evt: any) {
        var inputName = evt.currentTarget.name;
        var value = Number(evt.currentTarget.value);
        var id = Number(inputName.substring(inputName.indexOf('_') + 1));
        if (value == 0){
          this.selectedProductRequestList.splice(this.selectedProductRequestList.findIndex(prod => prod.produto.id == id), 1);
        }else{
          var selectedProductExisting = this.selectedProductRequestList.filter(prod => prod.produto.id == id)[0];
          selectedProductExisting.quantidade = value;
        }
    }
    inputQuantitySelectedProductPrepareOnChange(evt: any) {
        var inputName = evt.currentTarget.name;
        var value = Number(evt.currentTarget.value);
        var id = Number(inputName.substring(inputName.indexOf('_') + 1));
        if (value == 0){
          this.selectedProductPrepareList.splice(this.selectedProductPrepareList.findIndex(prod => prod.produto.id == id), 1);
        }else{
          var selectedProductExisting = this.selectedProductPrepareList.filter(prod => prod.produto.id == id)[0];
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
      alert();
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
                  },
                  error => {
                      this.alertService.catchError(error);
                  });;
    }
    clearPreparar(): void{
      this.selectedProductPrepareList = [];
    }
  
    preferencesRequestOnClick($event): void{
      alert();
    }
}