import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './core/services/auth.service';
import { SystemStatusService } from './core/services/system-status.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  readonly isAuthenticated$ = this.authService.isAuthenticated$;
  readonly user$ = this.authService.currentUser$;
  readonly backendUnavailable$ = this.systemStatus.backendUnavailable$;

  constructor(
    private readonly authService: AuthService,
    private readonly systemStatus: SystemStatusService
  ) {}

  onLogout(): void {
    this.authService.logout();
  }

  dismissBackendUnavailable(): void {
    this.systemStatus.resetBackendUnavailable();
  }

  retryConnection(): void {
    this.systemStatus.resetBackendUnavailable();
    window.location.reload();
  }
}
