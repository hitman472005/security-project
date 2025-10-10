import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Register } from '../../features/auth/register/register';
import { Registrar } from '../../models/registrar';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) { }

  // =========================
  // Listar todos los usuarios
  // =========================
  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users`);
  }
  // =========================
  // Crear un usuario
  // =========================
createUser(request: Registrar): Observable<any> {
  return this.http.post(`${this.backendUrl}/users`, request, {
    responseType: 'text'
  });
}

  // =========================
  // Buscar usuario por ID
  // =========================
  getUserById(id: number): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/users/${id}`);
  }

  // =========================
  // Buscar usuario por username
  // =========================
  getUserByUsername(username: string): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/users/username/${username}`);
  }

  // =========================
  // Buscar usuario por email
  // =========================
  getUserByEmail(email: string): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/users/email/${email}`);
  }

  // =========================
  // Actualizar usuario
  // =========================
  updateUser(userId: number, updatedUser: Registrar): Observable<any> {
    return this.http.put<any>(`${this.backendUrl}/users/${userId}`, updatedUser);
  }

}
