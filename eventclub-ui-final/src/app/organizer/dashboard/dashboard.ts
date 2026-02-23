import { Component, OnInit,AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EventService } from '../../core/services/event.service';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-organizer-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class DashboardComponent implements OnInit ,AfterViewInit{

  events: any[] = [];
  registeredEventIds: any[] = [];
  title = '';
  description = '';
  location = '';
  paid = false;
  fee = 0;
  router: any;
  message = '';
  selectedStats: any = null;
  loading = true;
  constructor(private eventService: EventService) {}

  ngAfterViewInit(): void {
    this.createChart();
  }

  createChart() {
    new Chart('myChart', {
      type: 'bar',
      data: {
        labels: ['Java Event', 'Python Event', 'CPP Event'],
        datasets: [{
          label: 'Registrations',
          data: [12, 19, 7],
          backgroundColor: '#4f46e5'
        }]
      }
    });
  }

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents() {
  this.loading = true;
  this.eventService.getPublishedEvents().subscribe(res => {
    this.events = res;
    this.loading = false;
  });
}

  createEvent() {
    const clubId = 1; // replace later with dynamic logic

    const payload = {
      title: this.title,
      description: this.description,
      location: this.location,
      paid: this.paid,
      fee: this.paid ? this.fee : 0
    };

    this.eventService.createEvent(clubId, payload).subscribe({
      next: () => {
        this.message = "Event created (Draft)";
        this.resetForm();
        this.loadEvents();
      },
      error: () => this.message = "Error creating event"
    });
  }

  publish(eventId: number) {
    this.eventService.publishEvent(eventId).subscribe({
      next: () => this.loadEvents(),
      error: () => alert("Publish failed")
    });
  }

  resetForm() {
    this.title = '';
    this.description = '';
    this.location = '';
    this.paid = false;
    this.fee = 0;
  }

  viewStats(eventId: number) {
  this.eventService.getEventStats(eventId).subscribe({
    next: (res) => this.selectedStats = res,
    error: () => alert("Failed to load stats")
  });
}

logout() {
  localStorage.clear();
  this.router.navigate(['/auth/login']);
}
}