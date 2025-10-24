import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './core/services/auth.service';

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

  constructor(private readonly authService: AuthService) {}

  onLogout(): void {
    this.authService.logout();
  }
}
