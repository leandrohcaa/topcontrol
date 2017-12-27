import { Injectable } from '@angular/core';

@Injectable()
export class WebServiceService {
    constructor() { }

    getURL(): string{
        return 'http://localhost:8080/api/';
    }
        
    catchResponsePOST(response: any): any{
        return response != null && response._body != ""  ? JSON.parse(response['_body']) : null;
    }
    
    catchResponseGET(response: any): any{
        return response != null && response._body != "" ? response.json() : null;
    }
}