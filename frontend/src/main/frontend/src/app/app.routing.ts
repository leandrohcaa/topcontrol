import { Routes, RouterModule } from '@angular/router';

import { RequestComponent } from './pages/request/index';
import { LoginComponent } from './pages/login/index';
import { RegisterComponent } from './pages/register/index';
import { AuthGuard } from './auth/_guards/index';

const appRoutes: Routes = [
    { path: '', component: RequestComponent, canActivate: [AuthGuard] },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },

    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);