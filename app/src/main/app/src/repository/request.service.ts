import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import 'rxjs/add/operator/map'

import { UsuarioNegocio } from '../models/index';
import { WebServiceService } from '../services/index';

@Injectable()
export class RequestRepository {
    constructor(private http: Http,
        private webServiceService: WebServiceService) { }

    concludePreparing(requisicaoProdutoId: number) {
        return this.http.get(this.webServiceService.getURL() + 'request/concludepreparing?requisicaoProdutoId=' + requisicaoProdutoId)
            .map((response: Response) => {
                return this.webServiceService.catchResponseGET(response);
            });
    }

    concludePayment(requisicaoProdutoId: number) {
        return this.http.get(this.webServiceService.getURL() + 'request/concludepayment?requisicaoProdutoId=' + requisicaoProdutoId)
            .map((response: Response) => {
                return this.webServiceService.catchResponseGET(response);
            });
    }
    
    cancelPreparing(requisicaoProdutoId: number) {
        return this.http.get(this.webServiceService.getURL() + 'request/cancelpreparing?requisicaoProdutoId=' + requisicaoProdutoId)
            .map((response: Response) => {
                return this.webServiceService.catchResponseGET(response);
            });
    }
    
    cancelRequest(requisicaoProdutoId: number) {
        return this.http.get(this.webServiceService.getURL() + 'request/cancelrequest?requisicaoProdutoId=' + requisicaoProdutoId)
            .map((response: Response) => {
                return this.webServiceService.catchResponseGET(response);
            });
    }

    getPreparingList(usuarioNegocio: UsuarioNegocio) {
        return this.http.get(this.webServiceService.getURL() + 'request/getpreparinglist?usuarioNegocioId=' + usuarioNegocio.id)
            .map((response: Response) => {
                return this.webServiceService.catchResponseGET(response);
            });
    }

    getForPaymentList(usuarioNegocio: UsuarioNegocio) {
        return this.http.get(this.webServiceService.getURL() + 'request/getforpaymentlist?usuarioNegocioId=' + usuarioNegocio.id)
            .map((response: Response) => {
                return this.webServiceService.catchResponseGET(response);
            });
    }
	
	getLastModificationRequisicaoProdutoByDono(usuarioNegocioId: number){
        return this.http.get(this.webServiceService.getURL() + 'request/getlastmodificationrequisicaoprodutobydono?usuarioNegocioId=' + usuarioNegocioId)
            .map((response: Response) => {
                return this.webServiceService.catchResponseGET(response);
            });
	}
}