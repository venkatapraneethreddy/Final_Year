import { Routes } from '@angular/router';
import { AdminLayoutComponent } from './layout/admin-layout.component';

export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    component: AdminLayoutComponent,
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./dashboard/admin-dashboard.component')
            .then(m => m.AdminDashboardComponent)
      },
      {
        path: 'club-approvals',
        loadComponent: () =>
          import('./club-approvals/club-approvals.component')
            .then(m => m.ClubApprovalsComponent)
      },
      {
        path: 'users',
        loadComponent: () =>
          import('./users/admin-users.component')
            .then(m => m.AdminUsersComponent)
      }
    ]
  }
];