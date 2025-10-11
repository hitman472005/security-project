
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot, UrlTree } from '@angular/router';

import { Observable } from 'rxjs';
import { GoogleService } from '../services/google.service';

@Injectable({
    providedIn: 'root'
})
export class UserGuard implements CanActivate {

    constructor(private authService: GoogleService, private router: Router) {

    }


    canActivate(route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {


const token = this.authService.token;
if (token) {
  return true; // usuario logueado → permite acceso
} else {
  return this.router.parseUrl('/login'); // no logueado → redirige a login
}

    }




}