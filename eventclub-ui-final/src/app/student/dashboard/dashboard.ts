import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventService } from '../../core/services/event.service';
import { RegistrationService } from '../../core/services/registration.service';
import { Observable } from 'rxjs';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-student-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class DashboardComponent {

  events$!: Observable<any[]>;
  registeredEventIds: number[] = [];
  events: any[] = [];
  message = '';
  router: any;
  loading = true;
  constructor(
    private eventService: EventService,
    private registrationService: RegistrationService,
    private toastService: ToastService
  ) {}

  ngOnInit() {
    this.events$ = this.eventService.getPublishedEvents();

    this.registrationService.getMyRegistrations().subscribe({
      next: (res) => {
        this.registeredEventIds = res.map(r => r.event.eventId);
      }
    });
  }

  register(eventId: number) {
    this.registrationService.register(eventId).subscribe({
      next: () => {
        this.toastService.show('Registered successfully');
        this.registeredEventIds = [...this.registeredEventIds, eventId];
      },
      error: () => {
        this.toastService.show('Already registered');
      }
    });
  }

  isRegistered(eventId: number): boolean {
    return this.registeredEventIds.includes(eventId);
  }

  logout() {
  localStorage.clear();
  this.router.navigate(['/auth/login']);

  
}
loadEvents() {
  this.loading = true;
  this.eventService.getPublishedEvents().subscribe(res => {
    this.events = res;
    this.loading = false;
  });
}
}