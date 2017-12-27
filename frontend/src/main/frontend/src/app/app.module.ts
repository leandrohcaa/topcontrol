import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule }    from '@angular/forms';
import { HttpModule } from '@angular/http';
import { FlexLayoutModule } from "@angular/flex-layout";

import {
    MatAutocompleteModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatDialogModule,
    MatExpansionModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatRippleModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
    MatStepperModule,
} from '@angular/material';

import { AppComponent } from './app.component';
import { routing } from './app.routing';

import { AuthGuard } from './auth/_guards/index';
import { AuthenticationRepository, ProductRepository, RequestRepository } from './repository/index';
import { AlertService, WebServiceService, PoolService, CommonsService } from './services/index';

import { RequestComponent } from './pages/request/index';
import { ContactComponent } from './pages/contact/index';
import { LoginComponent } from './pages/login/index';
import { RegisterComponent } from './pages/register/index';
import { DataConfigComponent } from './pages/dataConfig/index';

import { ModalAlert } from './commons/modal/alert/index';
import { ModalCaracteristicaPrepare } from './commons/modal/caracteristicaPrepare/index';
import { ModalLoading } from './commons/modal/loading/index';
import { ModalOwner } from './commons/modal/owner/index';


@NgModule({
    exports: [
        MatAutocompleteModule,
        MatButtonModule,
        MatButtonToggleModule,
        MatCardModule,
        MatCheckboxModule,
        MatChipsModule,
        MatStepperModule,
        MatDatepickerModule,
        MatDialogModule,
        MatExpansionModule,
        MatGridListModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatMenuModule,
        MatNativeDateModule,
        MatPaginatorModule,
        MatProgressBarModule,
        MatProgressSpinnerModule,
        MatRadioModule,
        MatRippleModule,
        MatSelectModule,
        MatSidenavModule,
        MatSliderModule,
        MatSlideToggleModule,
        MatSnackBarModule,
        MatSortModule,
        MatTableModule,
        MatTabsModule,
        MatToolbarModule,
        MatTooltipModule,
    ]
})
export class MaterialModule { }

@NgModule({
    declarations: [
        AppComponent,
        RequestComponent, ContactComponent, LoginComponent, RegisterComponent, DataConfigComponent,
        ModalAlert, ModalOwner, ModalLoading, ModalCaracteristicaPrepare
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        NoopAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        HttpModule,
        routing,
        MaterialModule,
        FlexLayoutModule
    ],
    providers: [
        AuthGuard,
        AuthenticationRepository, ProductRepository, RequestRepository,
        AlertService, WebServiceService, PoolService, CommonsService
    ],
    bootstrap: [AppComponent, ModalAlert, ModalOwner, ModalLoading, ModalCaracteristicaPrepare]
})
export class AppModule { }
