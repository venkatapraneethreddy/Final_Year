package com.college.eventclub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.college.eventclub.model.Event;
import com.college.eventclub.model.User;
import com.college.eventclub.service.EventRegistrationService;
import com.college.eventclub.service.EventService;
import com.college.eventclub.service.UserService;

@RestController
@RequestMapping("/api/registrations")
public class EventRegistrationController {

    private final EventRegistrationService eventRegistrationService;
    private final EventService eventService;
    private final UserService userService;

    public EventRegistrationController(EventRegistrationService eventRegistrationService,
                                       EventService eventService,
                                       UserService userService) {
        this.eventRegistrationService = eventRegistrationService;
        this.eventService = eventService;
        this.userService = userService;
    }

    // POST /api/registrations/{eventId} - Student registers
    @PostMapping("/{eventId}")
    public ResponseEntity<?> registerForEvent(@PathVariable Long eventId,
                                              Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Event event = eventService.getEventById(eventId);
            return new ResponseEntity<>(
                    eventRegistrationService.register(user, event),
                    HttpStatus.CREATED
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    // GET /api/registrations/my - Student sees their registrations
    @GetMapping("/my")
    public ResponseEntity<?> getMyRegistrations(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(eventRegistrationService.getRegistrationsByStudent(email));
    }

    // DELETE /api/registrations/{registrationId} - Student cancels
    @DeleteMapping("/{registrationId}")
    public ResponseEntity<?> cancelRegistration(@PathVariable Long registrationId,
                                                Authentication authentication) {
        try {
            String email = authentication.getName();
            eventRegistrationService.cancelRegistration(registrationId, email);
            return ResponseEntity.ok("Registration cancelled successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    // Feature 5: GET /api/registrations/event/{eventId} - Organizer views registrants
    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getEventRegistrants(@PathVariable Long eventId,
                                                  Authentication authentication) {
        try {
            String email = authentication.getName();
            return ResponseEntity.ok(
                eventRegistrationService.getRegistrantsForEvent(eventId, email)
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }
}
