import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ClubService } from '../../core/services/club.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-create-club',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-club.component.html',
  styleUrl: './create-club.component.scss',
})
export class CreateClubComponent {

  form!: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private clubService: ClubService,
    public router: Router,
    private toastr: ToastrService
  ) {
    this.form = this.fb.group({
      clubName: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', Validators.required]
    });
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;

    this.clubService.createClub(this.form.value).subscribe({
      next: () => {
        this.toastr.success('Club created! Awaiting admin approval.');
        this.router.navigate(['/organizer/dashboard']);
      },
      error: () => {
        this.toastr.error('Failed to create club');
        this.loading = false;
      }
    });
  }
}
