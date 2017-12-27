import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'

import { Usuario } from '../models/index';
import { WebServiceService } from '../services/index';
import { AuthenticationRepository } from './authentication.service';
import { GrupoProdutoProdutoDTO } from '../models/index';

@Injectable()
export class ProductRepository {
    constructor(private http: Http,
        private webServiceService: WebServiceService,
        private authenticationRepository: AuthenticationRepository) { }

    getProductAndGroup() {
        var owner = this.authenticationRepository.getOwner();
        var usuarioNegocioId = null;
        if (owner != null && owner.usuarioNegocioList != null && owner.usuarioNegocioList.length > 0)
            usuarioNegocioId = owner.usuarioNegocioList[0].id;
        return this.http.get(this.webServiceService.getURL() + 'product/productandgrouplist?usuarioNegocioId=' + usuarioNegocioId)
            .map((response: Response) => {
                return this.webServiceService.catchResponseGET(response);
            });
    }

    getProduct() {
        var owner = this.authenticationRepository.getOwner();
        var usuarioNegocioId = null;
        if (owner != null && owner.usuarioNegocioList != null && owner.usuarioNegocioList.length > 0)
            usuarioNegocioId = owner.usuarioNegocioList[0].id;
        return this.http.get(this.webServiceService.getURL() + 'product/productlist?usuarioNegocioId=' + usuarioNegocioId)
            .map((response: Response) => {
                return this.webServiceService.catchResponseGET(response);
            });
    }
    
    saveImage(product: GrupoProdutoProdutoDTO, image: string) {
        return this.http.post(this.webServiceService.getURL() + 'product/saveImage',
            { product: product, image: image });
    }
}