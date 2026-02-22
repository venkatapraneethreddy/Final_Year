import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegistrationService } from '../../core/services/registration.service';
import { ToastService } from '../../core/services/toast.service';
import { EventService } from '../../core/services/event.service';

@Component({
  selector: 'app-student-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class DashboardComponent {
  registeredEventIds: number[] = [];
  events: any[] = [];
  loading = true;

  constructor(
    private eventService: EventService,
    private registrationService: RegistrationService,
    private toastService: ToastService
  ) {}

  ngOnInit() {
    this.loadEvents();
    this.registrationService.getMyRegistrations().subscribe({
      next: (res) => {
        this.registeredEventIds = res.map(r => r.event.eventId);
      }
    });
  }

  loadEvents() {
    this.loading = true;
    this.eventService.getPublishedEvents().subscribe({
      next: (res) => {
        this.events = res;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  register(eventId: number) {
    this.registrationService.register(eventId).subscribe({
      next: () => {
        this.toastService.show('Registration successful. Payment captured if required.');
        this.registeredEventIds = [...this.registeredEventIds, eventId];
      },
      error: () => this.toastService.show('Already registered or event unavailable')
    });
  }

  isRegistered(eventId: number): boolean {
    return this.registeredEventIds.includes(eventId);
  }
}
