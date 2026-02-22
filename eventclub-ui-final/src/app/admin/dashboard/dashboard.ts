import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClubService } from '../../core/services/club.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class DashboardComponent implements OnInit {
  clubs: any[] = [];

  constructor(private clubService: ClubService) {}

  ngOnInit(): void {
    this.loadClubs();
  }

  loadClubs() {
    this.clubService.getPendingClubs().subscribe({
      next: (res) => (this.clubs = res),
      error: () => (this.clubs = [])
    });
  }

  approve(id: number) {
    this.clubService.approveClub(id).subscribe({
      next: () => this.loadClubs()
    });
  }
}
