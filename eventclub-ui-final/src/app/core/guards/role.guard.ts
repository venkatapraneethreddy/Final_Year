import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const roleGuard: CanActivateFn = (route) => {

  const router = inject(Router);
  const role = localStorage.getItem('role');

  const expectedRole = route.data?.['role'];

  if (!role || role !== expectedRole) {
    router.navigate(['/auth/login']);
    return false;
  }

  return true;
};