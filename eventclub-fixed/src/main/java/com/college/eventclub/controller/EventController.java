package com.college.eventclub.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;
    private final ClubService clubService;

    public EventController(EventService eventService, ClubService clubService) {
        this.eventService = eventService;
        this.clubService = clubService;
    }

    // POST /api/events/{clubId} - ORGANIZER creates event for their club
    @PostMapping("/{clubId}")
    public ResponseEntity<Event> createEvent(@PathVariable Long clubId,
                                             Authentication authentication,
                                             @RequestBody Event event) {

        String organizerEmail = authentication.getName();

        Club club = clubService.getApprovedClubs().stream()
                .filter(c -> c.getClubId().equals(clubId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Club not found or not approved"));

        // FIX: Ownership is verified inside the service
        Event savedEvent = eventService.createEventForClub(event, club, organizerEmail);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    // PUT /api/events/{eventId} - ORGANIZER updates their event
    @PutMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable Long eventId,
                                         Authentication authentication,
                                         @RequestBody Event updates) {
        try {
            String email = authentication.getName();
            Event updated = eventService.updateEvent(eventId, updates, email);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    // PUT /api/events/{eventId}/publish - ORGANIZER publishes their own event
    @PutMapping("/{eventId}/publish")
    public ResponseEntity<?> publishEvent(@PathVariable Long eventId,
                                          Authentication authentication) {
        try {
            String email = authentication.getName();

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            Event publishedEvent = isAdmin
                    ? eventService.adminPublishEvent(eventId)
                    : eventService.publishEvent(eventId, email); // FIX: ownership check

            return ResponseEntity.ok(publishedEvent);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    // DELETE /api/events/{eventId} - ORGANIZER cancels their event
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> cancelEvent(@PathVariable Long eventId,
                                         Authentication authentication) {
        try {
            String email = authentication.getName();
            eventService.cancelEvent(eventId, email);
            return ResponseEntity.ok("Event cancelled successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    // GET /api/events - returns only PUBLISHED events (students/all)
    @GetMapping
    public ResponseEntity<?> getPublishedEvents() {
        // FIX: was calling getAllEvents() - now correctly returns only PUBLISHED
        return ResponseEntity.ok(eventService.getPublishedEvents());
    }

    // GET /api/events/{id} - get single event by ID (NEW)
    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable Long eventId) {
        try {
            return ResponseEntity.ok(eventService.getEventById(eventId));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // GET /api/events/my - ORGANIZER sees their own events
    @GetMapping("/my")
    public ResponseEntity<?> getMyEvents(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(eventService.getEventsByOrganizer(email));
    }

    // GET /api/events/organizer/analytics
    @GetMapping("/organizer/analytics")
    public ResponseEntity<?> getAnalytics(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(eventService.getOrganizerAnalytics(email));
    }
}
