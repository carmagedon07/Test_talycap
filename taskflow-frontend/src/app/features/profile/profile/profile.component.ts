import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { finalize } from 'rxjs/operators';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
  readonly user$ = this.authService.currentUser$;
  isLoading = false;
  errorMessage = '';

  constructor(private readonly authService: AuthService) {}

  refreshProfile(): void {
    this.errorMessage = '';
    this.isLoading = true;
    this.authService
      .refreshProfile()
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        error: (error) => {
          this.errorMessage = error?.error?.message ?? 'No se pudo actualizar el perfil.';
        }
      });
  }

}
