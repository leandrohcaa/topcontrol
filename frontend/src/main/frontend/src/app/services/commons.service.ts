import { Injectable } from '@angular/core';

@Injectable()
export class CommonsService {

    constructor() {
    }
    
    isJsonString(str: string): boolean {
        try {
            JSON.parse(str);
        } catch (e) {
            return false;
        }
        return true;
    }

    localDateTimeToJSDate(data: any): Date {
        return new Date(
            data[0] != null ? data[0] : 0, 
            data[1] != null ? data[1] - 1 : 0, 
            data[2] != null ? data[2] : 0, 
            data[3] != null ? data[3] : 0, 
            data[4] != null ? data[4] : 0, 
            data[5] != null ? data[5] : 0);
    }
}