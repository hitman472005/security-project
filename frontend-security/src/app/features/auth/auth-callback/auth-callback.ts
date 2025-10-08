import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { AlertService } from '../../../core/services/alert.service';

@Component({
  selector: 'app-auth-callback',
  imports: [],
  templateUrl: './auth-callback.html',
  styleUrl: './auth-callback.css'
})
export class AuthCallback {
  constructor(
    private route: ActivatedRoute,
    private alertService: AlertService,
    private authService: AuthService,
    private router: Router
  ) { }


  ngOnInit() {
    this.callBack();

  }

  callBack() {
    const code = this.route.snapshot.queryParamMap.get('code');

    if (!code) {
      this.alertService.error('Error de autenticación', 'No se recibió el código de Google.');

      return;
    }

    // 2️⃣ Llamar al backend para intercambiar code por token
    this.authService.loginWithCode(code).subscribe({
      next: (res) => {
        console.log('✅ Login exitoso');
        console.log('Token:', res.token);
        console.log('Email:', res.email);
        console.log('Name:', res.name);

        // Guardar token en localStorage
        localStorage.setItem('jwt', res.token);

        // Redirigir a la página principal
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.alertService.error('Error', 'No se pudo completar la autenticación con Google');
        this.router.navigate(['/login']);
      },
    });

  }
}
