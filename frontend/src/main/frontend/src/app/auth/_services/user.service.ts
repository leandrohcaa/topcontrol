import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';

import { Usuario } from '../../models/index';
import { WebServiceService } from '../../services/index';

@Injectable()
export class UserService {
    constructor(
      private http: Http,
      private webServiceService: WebServiceService) { }
    
    getById(id: number) {
        return this.http.get(this.webServiceService.getURL() + 'usuario?id=' + id, this.jwt()).map((response: Response) => response.json());
    }

    create(user: Usuario) {
        return this.http.post(this.webServiceService.getURL() + 'usuario/save', [user], this.jwt()).map((response: Response) => response.json());
    }

    // private helper methods

    private jwt() {
        // create authorization header with jwt token
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (currentUser && currentUser.token) {
            let headers = new Headers({ 'Authorization': 'Bearer ' + currentUser.token, 'Content-Type': 'application/json' });
            return new RequestOptions({ headers: headers });
        }
    }
}