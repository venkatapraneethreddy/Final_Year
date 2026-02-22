import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login';
import { RegisterComponent } from './auth/register/register';
import { DashboardComponent as StudentDashboard } from './student/dashboard/dashboard';
import { DashboardComponent as OrganizerDashboard } from './organizer/dashboard/dashboard';
import { DashboardComponent as AdminDashboard } from './admin/dashboard/dashboard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  { path: 'auth/login', component: LoginComponent },
  { path: 'auth/register', component: RegisterComponent },

  { 
    path: 'student', 
    component: StudentDashboard,
    canActivate: [roleGuard],
    data: { role: 'STUDENT' }
  },

  { 
    path: 'organizer', 
    component: OrganizerDashboard,
    canActivate: [roleGuard],
    data: { role: 'ORGANIZER' }
  },

  {
    path: 'admin',
    component: AdminDashboard,
    canActivate: [roleGuard],
    data: { role: 'ADMIN' }
  },

  { path: '', redirectTo: 'auth/login', pathMatch: 'full' }
];