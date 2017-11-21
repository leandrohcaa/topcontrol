import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule }    from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { routing } from './app.routing';

import { AlertComponent } from './auth/_directives/index';
import { AuthGuard } from './auth/_guards/index';
import { AlertService, AuthenticationService, UserService } from './auth/_services/index';
import { HomeComponent } from './pages/home/index';
import { LoginComponent } from './pages/login/index';
import { RegisterComponent } from './pages/register/index';

@NgModule({
  declarations: [
    AppComponent,
    AlertComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent
  ],
  imports: [
      BrowserModule,
      FormsModule,
      HttpModule,
      routing
  ],
  providers: [
        AuthGuard,
        AlertService,
        AuthenticationService,
        UserService
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
