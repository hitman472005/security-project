import { Routes } from '@angular/router';
import { Principal } from './features/auth/principal/principal';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { AuthCallback } from './features/auth/auth-callback/auth-callback';
import { Dashboard } from './features/user/dashboard/dashboard';
import { AdminGuard } from './core/guards/admin.guard';
import { UserGuard } from './core/guards/user.guard';
import { NoAuthGuard } from './core/guards/noauth.guard';


export const routes: Routes = [

    // Página Principal
    { path: '', component: Principal, pathMatch: 'full' },

    // Login / Register (solo accesibles si NO está logueado)
    { path: 'login', component: Login, canActivate: [NoAuthGuard] },
    { path: 'register', component: Register, canActivate: [NoAuthGuard] },

    // Callback de autenticación (puede estar libre)
    { path: 'auth-callback', component: AuthCallback },

    // Dashboard (solo accesible si está logueado)
    { path: 'dashboard', component: Dashboard, canActivate: [UserGuard] },

    // Cualquier otra ruta redirige a la principal
    { path: '**', redirectTo: '', pathMatch: 'full' }
];
