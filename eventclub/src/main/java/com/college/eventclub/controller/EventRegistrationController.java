package com.college.eventclub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/{eventId}")
public ResponseEntity<?> registerForEvent(@PathVariable Long eventId,
                                          Authentication authentication) {
    try {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventService.getPublishedEvents().stream()
                .filter(e -> e.getEventId().equals(eventId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return new ResponseEntity<>(
                eventRegistrationService.register(user, event),
                HttpStatus.CREATED
        );

    } catch (RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
}
@GetMapping("/my")
public ResponseEntity<?> getMyRegistrations(Authentication authentication) {

    String email = authentication.getName();

    return ResponseEntity.ok(
            eventRegistrationService.getRegistrationsByStudent(email)
    );
}



}
