import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-principal',
  imports: [],
  templateUrl: './principal.html',
  styleUrl: './principal.css'
})
export class Principal {
register() {
this.router.navigate(['/register'])
}
 constructor(private router: Router) {} // Inyectamos Router

  iniciarSesion() {
    this.router.navigate(['/login']);
  }
}
