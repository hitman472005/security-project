import { Routes } from '@angular/router';
import { Principal } from './features/auth/principal/principal';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';

export const routes: Routes = [

    //Pagina Principal
    { path: '', component: Principal, pathMatch: 'full' },
    { path: 'login', component: Login, pathMatch: 'full' },
    { path: 'register', component: Register, pathMatch: 'full' }

];
