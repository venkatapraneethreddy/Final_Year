import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ClubService {

  private apiUrl = 'http://localhost:8080/api/clubs';
  private adminUrl = 'http://localhost:8080/api/admin/clubs';

  constructor(private http: HttpClient) {}

  getAllClubs(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getMyClub(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/my`);
  }

  createClub(data: any): Observable<any> {
    return this.http.post(this.apiUrl, data);
  }

  getPendingClubs(): Observable<any[]> {
    return this.http.get<any[]>(`${this.adminUrl}/pending`);
  }

  approveClub(clubId: number): Observable<any> {
    return this.http.put(`${this.adminUrl}/${clubId}/approve`, {});
  }

  rejectClub(clubId: number): Observable<any> {
    return this.http.put(`${this.adminUrl}/${clubId}/reject`, {});
  }
}
