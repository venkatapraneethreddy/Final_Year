import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ClubService {

  private BASE_URL = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  approveClub(id: number) {
    return this.http.put(
      `${this.BASE_URL}/clubs/${id}/approve`,
      {}
    );
  }

  getPendingClubs() {
    return this.http.get<any[]>(`${this.BASE_URL}/clubs/pending`);
  }
}