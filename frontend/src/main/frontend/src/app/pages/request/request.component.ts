import { Component, OnInit } from '@angular/core';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';
import { Product, Request, RequestProduct } from '../../models/index';

@Component({
    moduleId: module.id,
    templateUrl: 'request.component.html'
})
export class RequestComponent implements OnInit {
    productList : Array<Product>;
    preferencesRequestList : Array<Request>;
    preferencesPrepareList : Array<Product>;
    selectedProductRequestList : Array<RequestProduct> = [];
    selectedProductPrepareList : Array<RequestProduct> = [];
    productRequestInputFormControl: FormControl;
    productPrepareInputFormControl: FormControl;
    filteredProductRequestList: Observable<any[]>;
    filteredProductPrepareList: Observable<any[]>;
    
    constructor() {	  
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
		  
      this.productList = [
        <Product>({id: 1, name: 'Herbalife 1',selectable:true}),
        <Product>({id: 2, name: 'Herbalife 2',selectable:true}),
        <Product>({id: 3, name: 'Herbalife 3',selectable:true}),
        <Product>({id: 4, name: 'Herbalife 4',selectable:true}),
        <Product>({id: 5, name: 'Herbalife 5',selectable:true})];
      this.preferencesRequestList = [];
      this.preferencesPrepareList = [];   
    }
  
    filterProduct(productFilter: any) {
      var productFilterName = typeof(productFilter) == 'string' ? productFilter : productFilter.name;
      return this.productList.filter(product =>
        product.name.toLowerCase().indexOf(productFilterName.toLowerCase()) >= 0);
    }  
    productRequestOnSelect(evt: any) {
      var selectedProductRequest;
      var selectedProductExisting = this.selectedProductRequestList.filter(prod => prod.product.id == evt.option.value.id);
      if(selectedProductExisting.length > 0){
        selectedProductRequest = selectedProductExisting[0];
        selectedProductRequest.quantity++;
      }
      else{
		selectedProductRequest = <RequestProduct>({product: evt.option.value})
        selectedProductRequest.quantity = 1;
        this.selectedProductRequestList.push(selectedProductRequest);
      }
      
      this.productRequestInputFormControl.setValue(null);
    }
    productPrepareOnSelect(evt: any) {
      var selectedProductRequest;
      var selectedProductExisting = this.selectedProductPrepareList.filter(prod => prod.product.id == evt.option.value.id);
      if(selectedProductExisting.length > 0){
        selectedProductRequest = selectedProductExisting[0];
        selectedProductRequest.quantity++;
      }
      else{
		selectedProductRequest = <RequestProduct>({product: evt.option.value})
        selectedProductRequest.quantity = 1;
        this.selectedProductPrepareList.push(selectedProductRequest);
      }
      
      this.productPrepareInputFormControl.setValue(null);
    }
    productWithDisplay(product: any): string {
      return product ? product.name : '';
    }
	
    inputQuantitySelectedProductRequestOnChange(evt: any) {
        var inputName = evt.currentTarget.name;
        var value = Number(evt.currentTarget.value);
        var id = Number(inputName.substring(inputName.indexOf('_') + 1));
        if (value == 0){
          this.selectedProductRequestList.splice(this.selectedProductRequestList.findIndex(prod => prod.product.id == id), 1);
        }else{
          var selectedProductExisting = this.selectedProductRequestList.filter(prod => prod.product.id == id)[0];
          selectedProductExisting.quantity = value;
        }
    }
    inputQuantitySelectedProductPrepareOnChange(evt: any) {
        var inputName = evt.currentTarget.name;
        var value = Number(evt.currentTarget.value);
        var id = Number(inputName.substring(inputName.indexOf('_') + 1));
        if (value == 0){
          this.selectedProductPrepareList.splice(this.selectedProductPrepareList.findIndex(prod => prod.product.id == id), 1);
        }else{
          var selectedProductExisting = this.selectedProductPrepareList.filter(prod => prod.product.id == id)[0];
          selectedProductExisting.quantity = value;
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
    preparar(): void{
      alert();
    }
  
    preferencesRequestOnClick($event): void{
      alert();
    }
}