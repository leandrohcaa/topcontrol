import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import 'rxjs/add/operator/map'

import { Usuario } from '../models/index';
import { WebServiceService } from '../services/index';

@Injectable()
export class AuthenticationRepository {
    constructor(private http: Http,
      private webServiceService: WebServiceService) { }
  
    currentUser: Usuario = null;
    
    public login(user: Usuario) {
        return this.http.post(this.webServiceService.getURL() + 'user/login', user)
            .map((response: Response) => {
                    var result = this.webServiceService.catchResponsePOST(response);
                    this.currentUser = result;
                    return result;
            });
    }
  
    public getUser() : Usuario {
        return this.currentUser;
    }
   
    public logout() {
        this.currentUser = null;
    }
}