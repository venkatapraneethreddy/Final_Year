import { Component, OnInit } from '@angular/core';
import { Router,RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {

  role: string | null = null;

  constructor(public router: Router) {}

  get isAuthPage(): boolean {
    return this.router.url.includes('/auth');
  }

  ngOnInit(): void {
    this.role = localStorage.getItem('role');
  }

  logout() {
    localStorage.clear();
    window.location.href = '/auth/login';
  }

  toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
  }

  get dashboardTitle(): string {
  const role = localStorage.getItem('role');
  if (role === 'ADMIN') return 'Admin Panel';
  if (role === 'ORGANIZER') return 'Organizer Panel';
  if (role === 'STUDENT') return 'Student Panel';
  return 'Dashboard';
}
}