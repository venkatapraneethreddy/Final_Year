import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RegistrationService } from '../../core/services/registration.service';
import { QRCodeComponent } from 'angularx-qrcode';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-student-my-registrations',
  standalone: true,
  imports: [CommonModule, FormsModule, QRCodeComponent],
  templateUrl: './student-my-registrations.component.html',
  styleUrls: ['./student-my-registrations.component.scss']
})
export class StudentMyRegistrationsComponent implements OnInit {

  registrations: any[] = [];
  filteredRegistrations: any[] = [];
  loading = true;
  selectedRegistration: any = null;
  cancelling = new Set<number>();
  paying = new Set<number>();

  // Feature 7: search and filter
  searchTerm = '';
  timeFilter: 'all' | 'upcoming' | 'past' = 'all';

  constructor(
    private registrationService: RegistrationService,
    private toastr: ToastrService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.loadRegistrations();
  }

  get pendingCount(): number {
    return this.registrations.filter(r => r.status === 'PENDING_PAYMENT').length;
  }

  loadRegistrations() {
    this.loading = true;
    this.registrationService.getMyRegistrations().subscribe({
      next: (data) => {
        // Sort: upcoming first, then past
        this.registrations = data.sort((a, b) => {
          const aDate = new Date(a.event?.eventDate).getTime() || 0;
          const bDate = new Date(b.event?.eventDate).getTime() || 0;
          return aDate - bDate;
        });
        this.applyFilter();
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Failed to load registrations');
        this.loading = false;
      }
    });
  }

  applyFilter() {
    let result = this.registrations;
    const now = new Date();

    if (this.timeFilter === 'upcoming') {
      result = result.filter(r => r.event?.eventDate && new Date(r.event.eventDate) >= now);
    } else if (this.timeFilter === 'past') {
      result = result.filter(r => r.event?.eventDate && new Date(r.event.eventDate) < now);
    }

    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(r =>
        r.event?.title?.toLowerCase().includes(term) ||
        r.event?.club?.clubName?.toLowerCase().includes(term) ||
        r.event?.location?.toLowerCase().includes(term)
      );
    }

    this.filteredRegistrations = result;
  }

  openTicket(reg: any) { this.selectedRegistration = reg; }
  closeTicket() { this.selectedRegistration = null; }

  pay(registrationId: number) {
    if (this.paying.has(registrationId)) return;
    this.paying.add(registrationId);

    this.http.post(`http://localhost:8080/api/payments/${registrationId}`, {}).subscribe({
      next: () => {
        const reg = this.registrations.find(r => r.registrationId === registrationId);
        if (reg) reg.status = 'CONFIRMED';
        this.paying.delete(registrationId);
        this.toastr.success('Payment successful! You\'re confirmed.');
      },
      error: () => {
        this.paying.delete(registrationId);
        this.toastr.error('Payment failed. Please try again.');
      }
    });
  }

  cancelRegistration(registrationId: number) {
    if (this.cancelling.has(registrationId)) return;
    this.cancelling.add(registrationId);

    this.registrationService.cancelRegistration(registrationId).subscribe({
      next: () => {
        this.registrations = this.registrations.filter(r => r.registrationId !== registrationId);
        this.applyFilter();
        this.cancelling.delete(registrationId);
        this.toastr.success('Registration cancelled');
      },
      error: () => {
        this.cancelling.delete(registrationId);
        this.toastr.error('Failed to cancel registration');
      }
    });
  }

  isPast(date: string): boolean {
    return date ? new Date(date) < new Date() : false;
  }

  formatDate(date: string): string {
    if (!date) return 'N/A';
    return new Date(date).toLocaleDateString('en-IN', {
      day: 'numeric', month: 'short', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  }
}
