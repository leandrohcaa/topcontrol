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
          {grupoProdutoProdutoDTOList: prepareList, usuario: this.authenticationRepository.getUser()})
            .map((response: Response) => {
                    return JSON.parse(response['_body']);
              });
    }
  
    request(requestList: Array<GrupoProdutoProdutoDTO>) {
        return this.http.post(this.webServiceService.getURL() + 'request/request', 
          {grupoProdutoProdutoDTOList: requestList, usuario: this.authenticationRepository.getUser()})
            .map((response: Response) => {
                    return JSON.parse(response['_body']);
              });
    }
    
    getPrepareAndRequestList() {
        return this.http.post(this.webServiceService.getURL() + 'request/getprepareandrequestlist',
            this.authenticationRepository.getUser())
              .map((response: Response) => {
                      return JSON.parse(response['_body']);
                });
    }
}