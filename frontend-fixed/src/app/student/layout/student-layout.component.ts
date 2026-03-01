import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-student-layout',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './student-layout.component.html',
  styleUrls: ['./student-layout.component.scss']
})
export class StudentLayoutComponent {
  fullName: string;

  constructor(private authService: AuthService, private router: Router) {
    this.fullName = this.authService.getFullName() || 'Student';
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
