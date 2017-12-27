import { Injectable } from '@angular/core';
import {Subscription} from "rxjs";
import {TimerObservable} from "rxjs/observable/TimerObservable";


@Injectable()
export class PoolService {
    private sub: Subscription = null;

    constructor() {
    }

    setPool(functionCall: any, argument: any, offset: number, frequency: number) {
        if (this.sub != null) {
            this.sub.unsubscribe();
        }
        this.sub = TimerObservable.create(offset, frequency).subscribe(t => functionCall(argument));
    }

    setInterval(functionCall: any, argument: any, offset: number, frequency: number) {
        return TimerObservable.create(offset, frequency).subscribe(t => functionCall(argument));
    }

    clearInterval(sub: Subscription) {
        sub.unsubscribe();
    }
}