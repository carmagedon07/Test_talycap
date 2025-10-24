import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { SystemStatusService } from '../services/system-status.service';
import { TokenStorageService } from '../services/token-storage.service';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenStorage = inject(TokenStorageService);
  const authService = inject(AuthService);
  const systemStatus = inject(SystemStatusService);

  const token = tokenStorage.getToken();
  const isApiRequest = req.url.startsWith('http');

  const authReq = token && isApiRequest
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 0 || error.status === 503) {
        authService.logout(false);
        systemStatus.notifyBackendUnavailable();
      }

      if (error.status === 401) {
        authService.logout();
      }
      return throwError(() => error);
    })
  );
};
