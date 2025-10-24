import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest, UserRegistrationResponse } from '../models/auth.model';
import { UserProfile } from '../models/user.model';
import { TokenStorageService } from './token-storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = environment.apiUrl;
  private readonly currentUserSubject = new BehaviorSubject<UserProfile | null>(null);

  readonly currentUser$ = this.currentUserSubject.asObservable();
  readonly isAuthenticated$ = this.currentUser$.pipe(map(user => !!user));

  constructor(
    private readonly http: HttpClient,
    private readonly tokenStorage: TokenStorageService,
    private readonly router: Router
  ) {
    if (this.hasToken()) {
      this.refreshProfile().subscribe({
        error: () => this.logout(false)
      });
    }
  }

  login(credentials: LoginRequest): Observable<UserProfile> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap(response => this.tokenStorage.setToken(response.token)),
      switchMap(() => this.refreshProfile()),
      catchError(error => {
        this.tokenStorage.clear();
        return throwError(() => error);
      })
    );
  }

  register(payload: RegisterRequest): Observable<UserRegistrationResponse> {
    return this.http.post<UserRegistrationResponse>(`${this.apiUrl}/auth/register`, payload);
  }

  refreshProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.apiUrl}/users/profile`).pipe(
      tap(profile => this.currentUserSubject.next(profile))
    );
  }

  loadProfile(): Observable<UserProfile | null> {
    if (!this.hasToken()) {
      return of(null);
    }

    return this.refreshProfile().pipe(
      catchError(error => {
        this.logout(false);
        return of(null);
      })
    );
  }

  logout(redirect = true): void {
    this.tokenStorage.clear();
    this.currentUserSubject.next(null);
    if (redirect) {
      this.router.navigate(['/login']);
    }
  }

  hasToken(): boolean {
    return this.tokenStorage.isAuthenticated();
  }

  hasCurrentUser(): boolean {
    return this.currentUserSubject.value !== null;
  }

  getCurrentUser(): UserProfile | null {
    return this.currentUserSubject.value;
  }
}
