import { Component, OnInit } from '@angular/core';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';

@Component({
    moduleId: module.id,
    templateUrl: 'request.component.html'
})
export class RequestComponent implements OnInit {
    preferencesList : Array<{
          id: number, name: string, isSelectable: boolean
      }>;
    productRequestList : Array<{
          id: number, name: string, isSelectable: boolean
      }>;
    selectedProductRequest : Array<{
          id: number, name: string, isSelectable: boolean
      }> = [];
    filteredProductRequestList: Observable<any[]>;
  	productRequestInputFormControl: FormControl;
    
    constructor() {
  		this.productRequestInputFormControl = new FormControl();
  		this.filteredProductRequestList = this.productRequestInputFormControl.valueChanges
  			.startWith(null)
  			.map(product => product ? this.filterProductRequest(product) : this.productRequestList.slice());
    }   
  
    ngOnInit() {
      this.preferencesList = [
  			{id:1,name:'Herbalife 1',selectable:true},
  			{id:2,name:'Herbalife 2',selectable:true},
  			{id:3,name:'Herbalife 3',selectable:true},
  			{id:4,name:'Herbalife 4',selectable:true},
  			{id:5,name:'Herbalife 5',selectable:true}];		  
      this.productRequestList = [
        {id: 1, name: 'Herbalife 1',selectable:true},
        {id: 2, name: 'Herbalife 2',selectable:true},
        {id: 3, name: 'Herbalife 3',selectable:true},
        {id: 4, name: 'Herbalife 4',selectable:true},
        {id: 5, name: 'Herbalife 5',selectable:true}];
    }
  
    filterProductRequest(productFilter: any) {
      var productFilterName = typeof(productFilter) == 'string' ? productFilter : productFilter.name;
      return this.productRequestList.filter(product =>
        product.name.toLowerCase().indexOf(productFilterName.toLowerCase()) >= 0);
    }  
    productRequestOnSelect(evt: any) {
      this.selectedProductRequest.push(evt.option.value);
      this.productRequestInputFormControl.setValue(null);
    }
    productRequestWithDisplay(product: any): string {
      return product ? product.name : '';
    }
}