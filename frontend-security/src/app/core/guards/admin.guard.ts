
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot, UrlTree } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AdminGuard implements CanActivate {

      

    canActivate(route: ActivatedRouteSnapshot, 
        state: RouterStateSnapshot): MaybeAsync<GuardResult> {
        throw new Error('Method not implemented.');
    }




}