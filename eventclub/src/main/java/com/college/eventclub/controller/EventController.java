package com.college.eventclub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.college.eventclub.model.Club;
import com.college.eventclub.model.Event;
import com.college.eventclub.service.ClubService;
import com.college.eventclub.service.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final ClubService clubService;

    public EventController(EventService eventService,
                           ClubService clubService) {
        this.eventService = eventService;
        this.clubService = clubService;
    }

    @PostMapping("/{clubId}")
public ResponseEntity<Event> createEvent(@PathVariable Long clubId,
                                         Authentication authentication,
                                         @RequestBody Event event)
 {

        // 🔍 DEBUG LINE — ADD THIS
    System.out.println("AUTHORITIES = " + authentication.getAuthorities());

        boolean isOrganizer = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ORGANIZER"));

        if (!isOrganizer) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Club club = clubService.getApprovedClubs().stream()
                .filter(c -> c.getClubId().equals(clubId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Club not found or not approved"));

        Event savedEvent = eventService.createEventForClub(event, club);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{eventId}/publish")
public ResponseEntity<Event> publishEvent(@PathVariable Long eventId,
                                          Authentication authentication) {

    boolean isAdminOrOrganizer = authentication.getAuthorities().stream()
            .anyMatch(a ->
                a.getAuthority().equals("ROLE_ADMIN") ||
                a.getAuthority().equals("ROLE_ORGANIZER")
            );

    if (!isAdminOrOrganizer) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    Event publishedEvent = eventService.publishEvent(eventId);
    return ResponseEntity.ok(publishedEvent);
}

@GetMapping
public ResponseEntity<?> getPublishedEvents() {
    return ResponseEntity.ok(eventService.getAllEvents());
}

@GetMapping("/my")
public ResponseEntity<?> getMyEvents(Authentication authentication) {

    String email = authentication.getName();

    // find user by email
    // get club created by that user
    // return events of that club

    return ResponseEntity.ok(
            eventService.getEventsByOrganizer(email)
    );
}

}
