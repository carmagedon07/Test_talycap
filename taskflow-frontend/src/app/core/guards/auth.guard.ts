import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.hasToken()) {
    return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
  }

  if (authService.hasCurrentUser()) {
    return true;
  }

  return authService.loadProfile().pipe(
    map(user => {
      if (user) {
        return true;
      }

      return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
    })
  );
};
