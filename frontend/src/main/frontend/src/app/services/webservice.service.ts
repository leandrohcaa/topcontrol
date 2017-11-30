import { Injectable } from '@angular/core';

@Injectable()
export class WebServiceService {
    constructor() { }

    getURL(): string{
        return 'http://localhost:8080/api/';
    }
}