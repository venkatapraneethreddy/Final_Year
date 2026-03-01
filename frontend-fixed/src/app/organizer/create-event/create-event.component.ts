import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { EventService } from '../../core/services/event.service';
import { ClubService } from '../../core/services/club.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-create-event',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-event.component.html',
  styleUrl: './create-event.component.scss'
})
export class CreateEventComponent {

  form!: FormGroup;
  loading = false;
  clubId!: number;
  clubStatus: string = '';
  clubLoading = true;

  constructor(
    private fb: FormBuilder,
    private eventService: EventService,
    private clubService: ClubService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.form = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', Validators.required],
      location: ['', Validators.required],
      eventDate: ['', Validators.required],
      capacity: [null, [Validators.required, Validators.min(1)]],
      paid: [false],
      fee: [0, Validators.min(0)]
    });

    this.loadClub();
  }

  loadClub() {
    this.clubService.getMyClub().subscribe({
      next: (res: any) => {
        if (res?.club) {
          this.clubId = res.club.clubId;
          this.clubStatus = res.club.status;
        }
        this.clubLoading = false;
      },
      error: () => {
        this.toastr.error('Could not fetch club info');
        this.clubLoading = false;
      }
    });
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    if (this.clubStatus !== 'APPROVED') {
      this.toastr.warning('Your club must be approved before creating events');
      return;
    }

    this.loading = true;
    this.eventService.createEvent(this.clubId, this.form.value).subscribe({
      next: () => {
        this.toastr.success('Event created successfully');
        this.router.navigate(['/organizer/my-events']);
      },
      error: () => {
        this.toastr.error('Failed to create event');
        this.loading = false;
      }
    });
  }
  
  goToMyEvents() {
  this.router.navigate(['/organizer/my-events']);
}
}
