import { Component } from '@angular/core';
import { GoogleService } from '../../../core/services/google.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from '../../../core/services/alert.service';
import { Login_IS } from '../../../models/loginIS';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  formulario!: FormGroup;
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: GoogleService,
    private alertService: AlertService
  ) { }


  login() {
    this.authService.login();
  }
  operar() {
    if (this.formulario.valid) {
      const login: Login_IS = {
        login: this.formulario.get('login')?.value,
        password: this.formulario.get('password')?.value
      };

    }
    else {
      this.alertService.warning('Campos incompletos', 'Por favor, completa todos los campos requeridos.');
      this.formulario.markAllAsTouched();
    }

  }
  ngOnInit(): void {
    this.initForm();
  }


  initForm() {
    this.formulario = this.fb.group({
      login: ['', Validators.required],
      password: ['', Validators.required]
    });
  }
}
