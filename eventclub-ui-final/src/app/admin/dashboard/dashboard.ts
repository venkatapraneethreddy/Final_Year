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
  events: any[] = [];
  registeredEventIds: any[] = [];
  constructor(private clubService: ClubService) {}

  ngOnInit(): void {
    this.loadClubs();
  }

  loadClubs() {
  this.clubService.getPendingClubs().subscribe({
    next: (res) => {
      console.log("PENDING CLUBS:", res);   // 🔥 DEBUG
      this.clubs = res;
    },
    error: (err) => console.log("Error:", err)
  });
}

  approve(id: number) {
    this.clubService.approveClub(id).subscribe({
      next: () => this.loadClubs()
    });
  }

}