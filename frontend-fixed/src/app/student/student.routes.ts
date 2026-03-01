import { Routes } from '@angular/router';
import { StudentLayoutComponent } from './layout/student-layout.component';

export const STUDENT_ROUTES: Routes = [
  {
    path: '',
    component: StudentLayoutComponent,
    children: [
      {
        path: '',
        pathMatch: 'full',
        loadComponent: () =>
          import('./dashboard/student-dashboard.component')
            .then(m => m.StudentDashboardComponent)
      },
      {
        path: 'my-registrations',
        loadComponent: () =>
          import('./my-registrations/student-my-registrations.component')
            .then(m => m.StudentMyRegistrationsComponent)
      },
      {
        // Feature 3: Event detail page route
        path: 'event/:id',
        loadComponent: () =>
          import('./event-detail/event-detail.component')
            .then(m => m.EventDetailComponent)
      }
    ]
  }
];
