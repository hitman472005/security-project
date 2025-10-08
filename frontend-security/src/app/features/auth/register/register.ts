import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AlertService } from '../../../core/services/alert.service';
import { Registrar } from '../../../models/registrar';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true, // ✅ si usas imports en el decorador, esto es necesario
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register implements OnInit {
  volverLogin() {
    this.router.navigate(['/login'])
  }

  formulario!: FormGroup;

  constructor(
    private fb: FormBuilder, private router: Router,
    private authService: AuthService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.initForm(); // ✅ Aquí inicializas el formulario antes de renderizar
  }

  initForm() {
    this.formulario = this.fb.group({
      name: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  operar() {
    if (this.formulario.valid) {
      const usuario: Registrar = {
        name: this.formulario.get('name')?.value,
        username: this.formulario.get('username')?.value,
        email: this.formulario.get('email')?.value,
        password: this.formulario.get('password')?.value
      };
      console.log(usuario)
      this.alertService.success('Registro exitoso', `¡Bienvenido ${usuario.name}!`);
    } else {
      console.log("hola")
      this.alertService.warning('Campos incompletos', 'Por favor, completa todos los campos requeridos.');
      this.formulario.markAllAsTouched();
    }
  }

  registrarConGoogle() {
    this.authService.login();
  }
}
