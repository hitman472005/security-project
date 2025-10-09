import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { GoogleService } from '../services/google.service';

@Injectable({
  providedIn: 'root'
})
export class NoAuthGuard implements CanActivate {

  constructor(private googleService: GoogleService, private router: Router) {}

  canActivate(): Observable<boolean | UrlTree> {
    const token = this.googleService.token; // JWT guardado

    if (token) {
      // Usuario logueado → redirige al dashboard
      return of(this.router.parseUrl('/dashboard'));
    } else {
      // No logueado → puede entrar a login/register
      return of(true);
    }
  }
}
