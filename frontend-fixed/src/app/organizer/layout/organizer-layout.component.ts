import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './organizer-layout.component.html',
  styleUrl: './organizer-layout.component.scss',
})
export class OrganizerLayoutComponent {
  fullName: string;

  constructor(private authService: AuthService, private router: Router) {
    this.fullName = this.authService.getFullName() || 'Organizer';
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
