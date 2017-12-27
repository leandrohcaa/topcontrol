import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'

import { GrupoProdutoProdutoDTO } from '../models/index';
import { WebServiceService } from '../services/index';
import { AuthenticationRepository } from './authentication.service';

@Injectable()
export class RequestRepository {
    constructor(private http: Http,
        private webServiceService: WebServiceService,
        private authenticationRepository: AuthenticationRepository) { }

    prepare(prepareList: Array<GrupoProdutoProdutoDTO>) {
        return this.http.post(this.webServiceService.getURL() + 'request/prepare',
            { grupoProdutoProdutoDTOList: prepareList, usuario: this.authenticationRepository.getUser() })
            .map((response: Response) => {
                return this.webServiceService.catchResponsePOST(response);
            });
    }

    request(requestList: Array<GrupoProdutoProdutoDTO>) {
        return this.http.post(this.webServiceService.getURL() + 'request/request',
            { grupoProdutoProdutoDTOList: requestList, usuario: this.authenticationRepository.getUser() })
            .map((response: Response) => {
                return this.webServiceService.catchResponsePOST(response);
            });
    }

    getPrepareAndRequestList() {
        return this.http.post(this.webServiceService.getURL() + 'request/getprepareandrequestresumelist',
            this.authenticationRepository.getUser())
            .map((response: Response) => {
                return this.webServiceService.catchResponsePOST(response);
            });
    }

    getPrepareList() {
        return this.http.get(this.webServiceService.getURL() + 'request/getprepareresumelist?usuarioId=' + this.authenticationRepository.getUser().id)
            .map((response: Response) => {
                return this.webServiceService.catchResponseGET(response);
            });
    }

    getLastModificationRequisicaoProdutoByUser(usuarioId: number) {
        return this.http.get(this.webServiceService.getURL() + 'request/getlastmodificationrequisicaoprodutobyuser?usuarioId=' + usuarioId)
            .map((response: Response) => {
                return this.webServiceService.catchResponseGET(response);
            });
    }
}