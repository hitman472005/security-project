import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-sidebar-user',
imports: [CommonModule, RouterModule],

  templateUrl: './sidebar-user.html',
  styleUrl: './sidebar-user.css'
})
export class SidebarUser {
  constructor(
    private router: Router
  ) {

  }
  isActive(path: string): boolean {
    return this.router.url === path;
  }
  isLoggedIn = false;
  contenido: any;
  hayContenidoEnPagina(): boolean {
    // Verificar si la variable "contenido" tiene algún valor
    return !!this.contenido;
  }
  status = false;
  addToggle() {
    this.status = !this.status;
  }
  logout() {
    //   this.login.logout();
    window.location.href = '';
  }
}
