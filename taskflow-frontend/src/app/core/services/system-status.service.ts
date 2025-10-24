import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SystemStatusService {
  private readonly backendUnavailableSubject = new BehaviorSubject<boolean>(false);
  readonly backendUnavailable$ = this.backendUnavailableSubject.asObservable();

  constructor(private readonly router: Router) {}

  notifyBackendUnavailable(): void {
    if (!this.backendUnavailableSubject.value) {
      this.backendUnavailableSubject.next(true);
      this.router.navigate(['/login']);
    }
  }

  resetBackendUnavailable(): void {
    this.backendUnavailableSubject.next(false);
  }
}
