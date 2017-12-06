import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'


import { Usuario } from '../models/index';
import { WebServiceService } from '../services/index';

@Injectable()
export class ProductRepository {
    constructor(private http: Http,
      private webServiceService: WebServiceService) { }
  
    getHierarchy() {
        return this.http.get(this.webServiceService.getURL() + 'produto/hierarchy');
    }
  
    getHierarchyList() {
        return this.http.get(this.webServiceService.getURL() + 'produto/hierarchyList');
    }
}