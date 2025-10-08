import 'zone.js';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { provideBrowserGlobalErrorListeners, importProvidersFrom } from '@angular/core';
import { OAuthModule } from 'angular-oauth2-oidc';

import { App } from './app/app';
import { routes } from './app/app.routes';

bootstrapApplication(App, {
  providers: [
    provideHttpClient(),
    provideRouter(routes),
    provideBrowserGlobalErrorListeners(),
    importProvidersFrom(OAuthModule.forRoot())   // ✅ solo esto
  ]
});