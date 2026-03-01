import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-layout.component.html',
  styleUrl: './admin-layout.component.scss'
})
export class AdminLayoutComponent {
  fullName: string;

  constructor(private authService: AuthService, private router: Router) {
    this.fullName = this.authService.getFullName() || 'Admin';
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
