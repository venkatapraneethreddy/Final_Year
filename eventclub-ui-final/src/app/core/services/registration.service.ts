import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  private BASE_URL = 'http://localhost:8080/api/registrations';

  constructor(private http: HttpClient) {}

  register(eventId: number) {
    return this.http.post(`${this.BASE_URL}/${eventId}`, {});
  }

  getMyRegistrations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.BASE_URL}/my`);
  }
}