import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { EventService } from '../../core/services/event.service';
import { RegistrationService } from '../../core/services/registration.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-student-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './student-dashboard.component.html',
  styleUrl: './student-dashboard.component.scss'
})
export class StudentDashboardComponent implements OnInit {

  events: any[] = [];
  filteredEvents: any[] = [];
  loading = true;
  registeredEventIds = new Set<number>();
  registering = new Set<number>();
  showPast = false;

  searchTerm = '';
  filterType: 'all' | 'free' | 'paid' = 'all';

  constructor(
    private eventService: EventService,
    private registrationService: RegistrationService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadEvents();
    this.loadRegisteredEvents();
  }

  loadEvents() {
    this.eventService.getPublishedEvents().subscribe({
      next: (data: any[]) => {
        this.events = data;
        this.applyFilter();
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Failed to load events');
        this.loading = false;
      }
    });
  }

  loadRegisteredEvents() {
    this.registrationService.getMyRegistrations().subscribe({
      next: (data: any[]) => {
        data.forEach(reg => this.registeredEventIds.add(reg.event.eventId));
      }
    });
  }

  applyFilter() {
    let result = this.events;
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(e =>
        e.title?.toLowerCase().includes(term) ||
        e.description?.toLowerCase().includes(term) ||
        e.location?.toLowerCase().includes(term)
      );
    }
    if (this.filterType === 'free') result = result.filter(e => !e.paid);
    if (this.filterType === 'paid') result = result.filter(e => e.paid);
    this.filteredEvents = result;
  }

  // Feature 8: split filtered events into upcoming and past
  get upcomingEvents(): any[] {
    const now = new Date();
    return this.filteredEvents.filter(e => !e.eventDate || new Date(e.eventDate) >= now);
  }

  get pastEvents(): any[] {
    const now = new Date();
    return this.filteredEvents.filter(e => e.eventDate && new Date(e.eventDate) < now);
  }

  register(eventId: number) {
    if (this.registering.has(eventId)) return;
    this.registering.add(eventId);
    this.registrationService.register(eventId).subscribe({
      next: () => {
        this.registeredEventIds.add(eventId);
        this.registering.delete(eventId);
        this.toastr.success('Registered successfully!');
      },
      error: (err) => {
        this.registering.delete(eventId);
        const msg = typeof err.error === 'string' ? err.error : 'Registration failed';
        this.toastr.error(msg);
      }
    });
  }

  isPast(date: string): boolean {
    return date ? new Date(date) < new Date() : false;
  }

  formatDate(date: string): string {
    if (!date) return 'Date TBA';
    return new Date(date).toLocaleDateString('en-IN', {
      day: 'numeric', month: 'short', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  }
}
