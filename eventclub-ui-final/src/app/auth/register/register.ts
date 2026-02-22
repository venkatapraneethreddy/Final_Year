import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class RegisterComponent {
  fullName = '';
  email = '';
  password = '';
  role = 'STUDENT';

  constructor(
    private authService: AuthService,
    private router: Router,
    private toastService: ToastService
  ) {}

  register() {
    const payload = {
      fullName: this.fullName,
      email: this.email,
      password: this.password,
      role: this.role
    };

    this.authService.register(payload).subscribe({
      next: () => {
        this.toastService.show('Account created successfully 🎉');
        this.router.navigate(['/auth/login']);
      },
      error: () => {
        this.toastService.show('Registration failed ❌');
      }
    });
  }
}
