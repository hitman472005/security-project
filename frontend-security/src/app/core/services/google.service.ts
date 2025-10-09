import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of, tap } from 'rxjs';
import { environment } from '../../../environments/environments';
import { LoginAuth } from '../../models/loginAuth';

@Injectable({
  providedIn: 'root'
})
export class GoogleService {
  private backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) { }

  loginWithCode(code: string): Observable<LoginAuth> {
    return this.http.post<LoginAuth>(
      `${this.backendUrl}/auth/google/loginWithGoogle`,
      { code },
      { headers: { 'Content-Type': 'application/json' } }
    );
  }


  // =========================
  // Obtener token
  // =========================
  get token(): string | null {
    return localStorage.getItem('jwt');
  }

  // =========================
  // Guardar token
  // =========================
  setToken(token: string) {
    localStorage.setItem('jwt', token);
  }

  login() {
    this.http.get<{ url: string }>(`${this.backendUrl}/auth/google/login-url`).subscribe({
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

  // =========================
  // Logout: elimina token local y llama al backend
  // =========================
  logout(): Observable<any> {
  const token = localStorage.getItem('jwt');

  if (!token) {
    return of({ message: 'No token to logout' });
  }

  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });

  // 👇 Cambia responseType a 'text' para evitar el error
  return this.http.post(`${this.backendUrl}/auth/logout`, {}, { headers, responseType: 'text' as 'json' }).pipe(
    tap(response => {
  
      localStorage.removeItem('jwt');
    }),
    catchError(error => {
      return of(error);
    })
  );
}


}
