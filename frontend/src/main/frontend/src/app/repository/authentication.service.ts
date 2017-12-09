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
                    localStorage.setItem('currentUser', response['_body']);
            });
    }

    loginOwner(user: Usuario) {
        return this.http.post(this.webServiceService.getURL() + 'user/loginOwner', user)
            .map((response: Response) => {
                    var currentOwnerUser = response['_body'];
                    localStorage.setItem('currentOwnerUser', currentOwnerUser);
            });
    }
 
    logout() {
        localStorage.removeItem('currentUser');
    }
 
    logoutOwner() {
        localStorage.removeItem('currentOwnerUser');
    }
  
    getById(id: number) {
        return this.http.get(this.webServiceService.getURL() + 'user?id=' + id).map((response: Response) => response.json());
    }

    create(user: Usuario) {
        return this.http.post(this.webServiceService.getURL() + 'user/save', [user]);
    }
}