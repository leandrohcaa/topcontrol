import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'


import { Usuario } from '../models/index';
import { WebServiceService } from '../services/index';

@Injectable()
export class AuthenticationRepository {
    constructor(private http: Http,
      private webServiceService: WebServiceService) { }

    getOwner(): Usuario {
      if(localStorage.getItem('currentOwnerUser') != null){
        return JSON.parse(localStorage.getItem('currentOwnerUser')) as Usuario;
      }
      return null;
    }
  
    getUser(): Usuario {
      if(localStorage.getItem('currentUser') != null){
        return JSON.parse(localStorage.getItem('currentUser')) as Usuario;
      }
      return null;
    }
  
    login(user: Usuario) {
        return this.http.post(this.webServiceService.getURL() + 'user/login', user)
            .map((response: Response) => {
                var result = this.webServiceService.catchResponsePOST(response);
                localStorage.setItem('currentUser', JSON.stringify(result));
                return result;
            });
    }

    loginOwner(user: Usuario) {
        return this.http.post(this.webServiceService.getURL() + 'user/loginOwner', user)
            .map((response: Response) => {
                var result = this.webServiceService.catchResponsePOST(response);
                localStorage.setItem('currentOwnerUser', JSON.stringify(result));
                return result;
            });
    }
 
    logout() {
        localStorage.removeItem('currentUser');
    }
 
    logoutOwner(user: Usuario) {
        return this.http.post(this.webServiceService.getURL() + 'user/loginOwner', user)
            .map((response: Response) => {
                localStorage.removeItem('currentOwnerUser');
                return this.webServiceService.catchResponsePOST(response);
            });
    }

    create(user: Usuario) {
        return this.http.post(this.webServiceService.getURL() + 'user/save', [user])
            .map((response: Response) => {
                return this.webServiceService.catchResponsePOST(response);
            });
    }
}