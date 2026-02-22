import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { EventService } from '../../core/services/event.service';

@Component({
  selector: 'app-organizer-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class DashboardComponent implements OnInit {
  events: any[] = [];
  title = '';
  description = '';
  location = '';
  paid = false;
  fee = 0;
  message = '';
  selectedStats: any = null;
  loading = true;

  get publishedCount(): number {
    return this.events.filter((event) => event.status === "PUBLISHED").length;
  }

  constructor(private eventService: EventService, private router: Router) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents() {
    this.loading = true;
    this.eventService.getMyEvents().subscribe({
      next: (res) => {
        this.events = res;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  createEvent() {
    const payload = {
      title: this.title,
      description: this.description,
      location: this.location,
      paid: this.paid,
      fee: this.paid ? this.fee : 0
    };

    this.eventService.createEvent(1, payload).subscribe({
      next: () => {
        this.message = 'Event created as draft.';
        this.resetForm();
        this.loadEvents();
      },
      error: () => (this.message = 'Could not create event. Ensure club is approved.')
    });
  }

  publish(eventId: number) {
    this.eventService.publishEvent(eventId).subscribe({
      next: () => this.loadEvents(),
      error: () => alert('Publish failed')
    });
  }

  viewStats(eventId: number) {
    this.eventService.getEventStats(eventId).subscribe({
      next: (res) => (this.selectedStats = res),
      error: () => alert('Failed to load stats')
    });
  }

  resetForm() {
    this.title = '';
    this.description = '';
    this.location = '';
    this.paid = false;
    this.fee = 0;
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/auth/login']);
  }
}
