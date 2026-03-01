import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClubService } from '../../core/services/club.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-club-approvals',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './club-approvals.component.html',
  styleUrl: './club-approvals.component.scss'
})
export class ClubApprovalsComponent implements OnInit {

  clubs: any[] = [];
  loading = true;
  processingId: number | null = null;

  constructor(
    private clubService: ClubService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadClubs();
  }

  loadClubs() {
    this.clubService.getPendingClubs().subscribe({
      next: (data: any[]) => {
        this.clubs = data;
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Failed to load clubs');
        this.loading = false;
      }
    });
  }

  approve(clubId: number) {
    this.processingId = clubId;
    this.clubService.approveClub(clubId).subscribe({
      next: () => {
        this.clubs = this.clubs.filter(c => c.clubId !== clubId);
        this.processingId = null;
        this.toastr.success('Club approved successfully');
      },
      error: () => {
        this.processingId = null;
        this.toastr.error('Approval failed');
      }
    });
  }

  reject(clubId: number) {
    this.processingId = clubId;
    this.clubService.rejectClub(clubId).subscribe({
      next: () => {
        this.clubs = this.clubs.filter(c => c.clubId !== clubId);
        this.processingId = null;
        this.toastr.warning('Club rejected');
      },
      error: () => {
        this.processingId = null;
        this.toastr.error('Rejection failed');
      }
    });
  }
}
