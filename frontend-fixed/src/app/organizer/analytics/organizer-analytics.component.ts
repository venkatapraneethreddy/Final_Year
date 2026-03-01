import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration } from 'chart.js';
import { EventService } from '../../core/services/event.service';

@Component({
  selector: 'app-organizer-analytics',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  templateUrl: './organizer-analytics.component.html',
  styleUrl: './organizer-analytics.component.scss'
})
export class OrganizerAnalyticsComponent implements OnInit {

  stats: any = { totalEvents: 0, totalRegistrations: 0 };
  loading = true;

  barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [{
      data: [],
      label: 'Registrations per Event',
      backgroundColor: '#3b82f6'
    }]
  };

  barChartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
  };

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.eventService.getAnalytics().subscribe({
      next: (data) => {
        this.animateValue(data.totalEvents, v => this.stats = { ...this.stats, totalEvents: v });
        this.animateValue(data.totalRegistrations, v => this.stats = { ...this.stats, totalRegistrations: v });

        this.barChartData = {
          labels: data.eventStats?.map((e: any) => e.eventTitle || 'Untitled') ?? [],
          datasets: [{
            data: data.eventStats?.map((e: any) => e.registrations) ?? [],
            label: 'Registrations per Event',
            backgroundColor: '#3b82f6'
          }]
        };

        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  animateValue(finalValue: number, update: (val: number) => void) {
    let current = 0;
    const totalFrames = 50;
    const increment = finalValue / totalFrames;
    const interval = setInterval(() => {
      current += increment;
      if (current >= finalValue) {
        current = finalValue;
        clearInterval(interval);
      }
      update(Math.floor(current));
    }, 16);
  }
}
