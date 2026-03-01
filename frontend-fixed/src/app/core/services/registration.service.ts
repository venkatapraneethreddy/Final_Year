import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class RegistrationService {

  private apiUrl = 'http://localhost:8080/api/registrations';

  constructor(private http: HttpClient) {}

  register(eventId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${eventId}`, {});
  }

  cancelRegistration(registrationId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${registrationId}`);
  }

  getMyRegistrations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/my`);
  }

  getMyRegistrationEventIds(): Observable<number[]> {
    return this.getMyRegistrations().pipe(
      map(regs => regs.map(r => r.event.eventId))
    );
  }
}
