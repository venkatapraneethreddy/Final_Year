import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../core/services/admin.service';
import { ToastrService } from 'ngx-toastr';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss'
})
export class AdminDashboardComponent implements OnInit {

  stats: any = {};
  loading = true;

  pieChartData: ChartConfiguration<'pie'>['data'] = {
    labels: ['Approved Clubs', 'Pending Clubs'],
    datasets: [{
      data: [],
      backgroundColor: ['#22c55e', '#facc15']
    }]
  };

  barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: ['Users', 'Events', 'Registrations'],
    datasets: [{
      data: [],
      label: 'System Overview',
      backgroundColor: '#2563eb'
    }]
  };

  constructor(
    private adminService: AdminService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {

  this.adminService.getStats().subscribe({
    next: (data) => {

      this.loading = false;

      this.runAnimation(data);

      // charts
      this.pieChartData.datasets[0].data = [
        data.totalClubs - data.pendingClubs,
        data.pendingClubs
      ];

      this.barChartData.datasets[0].data = [
        data.totalUsers,
        data.totalEvents,
        data.totalRegistrations
      ];
    },
    error: () => {
      this.loading = false;
    }
  });
}

 animateValue(
  finalValue: number,
  update: (val: number) => void
) {
  let current = 0;
  const duration = 800;
  const frameRate = 16; // smoother
  const totalFrames = duration / frameRate;
  const increment = finalValue / totalFrames;

  const interval = setInterval(() => {
    current += increment;

    if (current >= finalValue) {
      current = finalValue;
      clearInterval(interval);
    }

    update(Math.floor(current));
  }, frameRate);
}

private runAnimation(realData: any) {

  this.stats = {
    totalUsers: 0,
    totalClubs: 0,
    pendingClubs: 0,
    totalEvents: 0,
    totalRegistrations: 0
  };

  this.animateValue(realData.totalUsers, v =>
    this.stats = { ...this.stats, totalUsers: v });

  this.animateValue(realData.totalClubs, v =>
    this.stats = { ...this.stats, totalClubs: v });

  this.animateValue(realData.pendingClubs, v =>
    this.stats = { ...this.stats, pendingClubs: v });

  this.animateValue(realData.totalEvents, v =>
    this.stats = { ...this.stats, totalEvents: v });

  this.animateValue(realData.totalRegistrations, v =>
    this.stats = { ...this.stats, totalRegistrations: v });
}
}