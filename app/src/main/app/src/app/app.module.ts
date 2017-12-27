import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';
import { HttpModule } from '@angular/http';

import { AuthenticationRepository, RequestRepository } from '../repository/index';
import { WebServiceService, AlertService, PoolService, CommonsService } from '../services/index';

import { ModalAlert } from '../commons/modal/alert/index';
import { ModalContact } from '../commons/modal/contact/index';
import { ModalConfigADM } from '../commons/modal/configADM/index';
import { ModalDetailCook } from '../commons/modal/detailCook/index';
import { ModalDetailPay } from '../commons/modal/detailPay/index';

import { LoginPageModule } from '../pages/login/login.module';
import { CookPageModule } from '../pages/cook/cook.module';
import { PayPageModule } from '../pages/pay/pay.module';
 
import { MyApp } from './app.component';
 
@NgModule({
  declarations: [
    MyApp,
    ModalAlert, ModalContact, ModalConfigADM, ModalDetailCook, ModalDetailPay
  ],
  imports: [
    BrowserModule, BrowserAnimationsModule,
    HttpModule,
    IonicModule.forRoot(MyApp),
    LoginPageModule, CookPageModule, PayPageModule
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    ModalAlert, ModalContact, ModalConfigADM, ModalDetailCook, ModalDetailPay
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    AuthenticationRepository, RequestRepository,
    WebServiceService, AlertService, PoolService, CommonsService
  ]
})
export class AppModule {}