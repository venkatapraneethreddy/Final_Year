import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { EventService } from '../../core/services/event.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-my-events',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './my-events.component.html',
  styleUrl: './my-events.component.scss'
})
export class MyEventsComponent implements OnInit {

  events: any[] = [];
  loading = true;
  publishing = new Set<number>();
  cancelling = new Set<number>();

  constructor(
    private eventService: EventService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents() {
    this.loading = true;
    this.eventService.getMyEvents().subscribe({
      next: (data: any[]) => {
        this.events = data;
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Failed to load events');
        this.loading = false;
      }
    });
  }

  publish(eventId: number) {
    if (this.publishing.has(eventId)) return;
    this.publishing.add(eventId);

    this.eventService.publishEvent(eventId).subscribe({
      next: () => {
        const event = this.events.find(e => e.eventId === eventId);
        if (event) event.status = 'PUBLISHED';
        this.publishing.delete(eventId);
        this.toastr.success('Event published successfully');
      },
      error: () => {
        this.publishing.delete(eventId);
        this.toastr.error('Failed to publish event');
      }
    });
  }

  cancelEvent(eventId: number) {
    if (this.cancelling.has(eventId)) return;
    this.cancelling.add(eventId);

    this.eventService.cancelEvent(eventId).subscribe({
      next: () => {
        const event = this.events.find(e => e.eventId === eventId);
        if (event) event.status = 'CANCELLED';
        this.cancelling.delete(eventId);
        this.toastr.success('Event cancelled');
      },
      error: () => {
        this.cancelling.delete(eventId);
        this.toastr.error('Failed to cancel event');
      }
    });
  }

  formatDate(date: string): string {
    if (!date) return 'Date TBA';
    return new Date(date).toLocaleDateString('en-IN', {
      day: 'numeric', month: 'short', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  }
}
