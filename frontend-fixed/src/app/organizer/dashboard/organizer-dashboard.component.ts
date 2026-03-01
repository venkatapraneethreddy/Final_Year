import { Component, OnInit , NgZone} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClubService } from '../../core/services/club.service';
import { RouterModule, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-organizer-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './organizer-dashboard.component.html'
})
export class OrganizerDashboardComponent implements OnInit {

  club: any = undefined;
  loading = true;

  constructor(
    private clubService: ClubService,
    private route: ActivatedRoute,
    private zone: NgZone
  ) {}

  ngOnInit(): void {
    this.loadClub();

    this.route.url.subscribe(() => {
      this.loadClub();
    });
  }

  loadClub() {
  this.clubService.getMyClub().subscribe({
    next: (res: any) => {
      this.zone.run(() => {
        this.club = res.club;
        this.loading = false;
      });
    },
    error: () => {
      this.zone.run(() => {
        this.club = null;
        this.loading = false;
      });
    }
  });
}
}