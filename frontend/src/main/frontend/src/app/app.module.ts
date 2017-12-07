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
import { AlertService } from './services/index';
import { WebServiceService } from './services/index';

import { RequestComponent } from './pages/request/index';
import { ContactComponent } from './pages/contact/index';
import { LoginComponent } from './pages/login/index';
import { RegisterComponent } from './pages/register/index';
import { ModalAlert } from './commons/modal/alert/index';
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
export class MaterialModule {}

@NgModule({
  declarations: [
    AppComponent,
	  RequestComponent,
    ContactComponent,
    LoginComponent,
    RegisterComponent,
    ModalAlert,
    ModalOwner
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
        AlertService,
        AuthenticationRepository,
        ProductRepository,
        RequestRepository,
        WebServiceService
    ],
  bootstrap: [AppComponent, ModalAlert, ModalOwner]
})
export class AppModule { }
