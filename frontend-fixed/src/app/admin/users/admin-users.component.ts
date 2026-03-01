import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-users.component.html',
  styleUrl: './admin-users.component.scss',
})
export class AdminUsersComponent implements OnInit {

  users: any[] = [];
  filteredUsers: any[] = [];
  loading = true;
  searchTerm = '';
  roleFilter: 'ALL' | 'STUDENT' | 'ORGANIZER' | 'ADMIN' = 'ALL';
  deletingId: number | null = null;

  constructor(private http: HttpClient, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;
    this.http.get<any[]>('http://localhost:8080/api/admin/users').subscribe({
      next: (data) => {
        this.users = data;
        this.applyFilter();
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Failed to load users');
        this.loading = false;
      }
    });
  }

  applyFilter() {
    let result = this.users;

    if (this.roleFilter !== 'ALL') {
      result = result.filter(u => u.role === this.roleFilter);
    }

    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(u =>
        u.fullName?.toLowerCase().includes(term) ||
        u.email?.toLowerCase().includes(term)
      );
    }

    this.filteredUsers = result;
  }

  deleteUser(userId: number, name: string) {
    if (!confirm(`Are you sure you want to delete "${name}"? This cannot be undone.`)) return;

    this.deletingId = userId;
    this.http.delete(`http://localhost:8080/api/admin/users/${userId}`).subscribe({
      next: () => {
        this.users = this.users.filter(u => u.userId !== userId);
        this.applyFilter();
        this.deletingId = null;
        this.toastr.success('User deleted');
      },
      error: () => {
        this.deletingId = null;
        this.toastr.error('Failed to delete user');
      }
    });
  }

  get studentCount() { return this.users.filter(u => u.role === 'STUDENT').length; }
  get organizerCount() { return this.users.filter(u => u.role === 'ORGANIZER').length; }
  get adminCount() { return this.users.filter(u => u.role === 'ADMIN').length; }
}
