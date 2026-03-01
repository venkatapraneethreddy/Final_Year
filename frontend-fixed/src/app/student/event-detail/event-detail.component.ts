import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { EventService } from '../../core/services/event.service';
import { RegistrationService } from '../../core/services/registration.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-event-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './event-detail.component.html',
  styleUrls: ['./event-detail.component.scss']
})
export class EventDetailComponent implements OnInit {

  event: any = null;
  loading = true;
  registering = false;
  alreadyRegistered = false;
  notFound = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private eventService: EventService,
    private registrationService: RegistrationService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    const eventId = Number(this.route.snapshot.paramMap.get('id'));

    // Load event details and registration status in parallel
    this.eventService.getEventById(eventId).subscribe({
      next: (data) => {
        this.event = data;
        this.loading = false;
        this.checkIfRegistered(eventId);
      },
      error: () => {
        this.notFound = true;
        this.loading = false;
      }
    });
  }

  checkIfRegistered(eventId: number) {
    this.registrationService.getMyRegistrationEventIds().subscribe({
      next: (ids: number[]) => {
        this.alreadyRegistered = ids.includes(eventId);
      }
    });
  }

  register() {
    if (this.registering || this.alreadyRegistered) return;
    this.registering = true;

    this.registrationService.register(this.event.eventId).subscribe({
      next: () => {
        this.alreadyRegistered = true;
        this.registering = false;
        this.toastr.success('Successfully registered!');
      },
      error: (err) => {
        this.registering = false;
        const msg = typeof err.error === 'string' ? err.error : 'Registration failed';
        this.toastr.error(msg);
      }
    });
  }

  goBack() {
    this.router.navigate(['/student']);
  }

  formatDate(date: string): string {
    if (!date) return 'Date TBA';
    return new Date(date).toLocaleDateString('en-IN', {
      weekday: 'long', day: 'numeric', month: 'long',
      year: 'numeric', hour: '2-digit', minute: '2-digit'
    });
  }

  isPastEvent(): boolean {
    if (!this.event?.eventDate) return false;
    return new Date(this.event.eventDate) < new Date();
  }
}
