import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private BASE_URL = 'http://localhost:8080/api/events';

  constructor(private http: HttpClient) {}

  getPublishedEvents(): Observable<any[]> {
    return this.http.get<any[]>(this.BASE_URL);
  }
  createEvent(clubId: number, event: any) {
  return this.http.post(`${this.BASE_URL}/${clubId}`, event);
}

getMyEvents() {
  return this.http.get<any[]>(`${this.BASE_URL}/my`);
}

publishEvent(eventId: number) {
  return this.http.put(`${this.BASE_URL}/${eventId}/publish`, {});
}

getEventStats(eventId: number) {
  return this.http.get<any>(
    `http://localhost:8080/api/analytics/events/${eventId}`
  );
}
}