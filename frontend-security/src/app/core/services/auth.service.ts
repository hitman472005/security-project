import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';
import { LoginAuth } from '../../models/loginAuth';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) { }

  loginWithCode(code: string): Observable<LoginAuth> {
    return this.http.post<LoginAuth>(
      this.backendUrl,
      { code },
      { headers: { 'Content-Type': 'application/json' } }
    );
  }

  logout() {
    localStorage.removeItem('jwt');
  }

  get token(): string | null {
    return localStorage.getItem('jwt');
  }

  login() {
    this.http.get<{ url: string }>(`${this.backendUrl}/login-url`).subscribe({
      next: (response) => {
        window.location.href = response.url; // Redirige a Google
      },
      error: (err) => {
        console.error('Error obteniendo URL de login', err);
      },
    });
  }

  handleAuthCallback(code: string) {
    return this.http.post<{ jwt: string }>(`${this.backendUrl}/callback`, {
      code,
    });
  }


}
