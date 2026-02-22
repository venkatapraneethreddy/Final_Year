import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class LoginComponent {

  email = '';
  password = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login() {
  this.authService.login({
    email: this.email,
    password: this.password
  }).subscribe({
    next: () => {

      const role = localStorage.getItem('role');

      if (role === 'STUDENT') {
        this.router.navigate(['/student']);

      } else if (role === 'ORGANIZER') {
        this.router.navigate(['/organizer']);

      } else if (role === 'ADMIN') {
        this.router.navigate(['/admin']);   // 🔥 ADD THIS

      } else {
        this.router.navigate(['/auth/login']);
      }
    },
    error: () => alert('Invalid credentials')
  });
}
}