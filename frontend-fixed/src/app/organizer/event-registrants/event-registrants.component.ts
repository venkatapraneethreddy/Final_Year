import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-event-registrants',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './event-registrants.component.html',
  styleUrls: ['./event-registrants.component.scss']
})
export class EventRegistrantsComponent implements OnInit {

  eventId!: number;
  eventTitle = '';
  registrants: any[] = [];
  filteredRegistrants: any[] = [];
  loading = true;
  searchTerm = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.eventId = Number(this.route.snapshot.paramMap.get('eventId'));
    this.loadRegistrants();
  }

  loadRegistrants() {
    this.loading = true;
    this.http.get<any[]>(`http://localhost:8080/api/registrations/event/${this.eventId}`)
      .subscribe({
        next: (data) => {
          this.registrants = data;
          this.filteredRegistrants = data;
          this.loading = false;
        },
        error: () => {
          this.toastr.error('Failed to load registrants');
          this.loading = false;
        }
      });
  }

  applySearch() {
    const term = this.searchTerm.toLowerCase().trim();
    if (!term) {
      this.filteredRegistrants = this.registrants;
      return;
    }
    this.filteredRegistrants = this.registrants.filter(r =>
      r.studentName?.toLowerCase().includes(term) ||
      r.studentEmail?.toLowerCase().includes(term)
    );
  }

  get confirmedCount(): number {
    return this.registrants.filter(r => r.status === 'CONFIRMED').length;
  }

  get pendingCount(): number {
    return this.registrants.filter(r => r.status === 'PENDING_PAYMENT').length;
  }

  formatDate(date: string): string {
    if (!date) return 'N/A';
    return new Date(date).toLocaleDateString('en-IN', {
      day: 'numeric', month: 'short', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  }

  goBack() {
    this.router.navigate(['/organizer/my-events']);
  }
}
