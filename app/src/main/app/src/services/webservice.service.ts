import { Injectable } from '@angular/core';

@Injectable()
export class WebServiceService {
    host: string = 'http://localhost:8080/api/';    
    
    constructor() { }

    getURL(): string{
        return this.host;
    }
    
    setURL(newHost: string): void{
        this.host = newHost;
    }
        
    catchResponseGET(response: any): any{
        return response != null && response._body != "" ? response.json() : null;
    }
    
    catchResponsePOST(response: any): any{
        return response != null && response._body != ""  ? JSON.parse(response['_body']) : null;
    }
}