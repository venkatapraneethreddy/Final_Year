package com.college.eventclub.controller;

import com.college.eventclub.model.Event;
import com.college.eventclub.model.dto;
import com.college.eventclub.service.InMemoryPlatformService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final InMemoryPlatformService service;

    public EventController(InMemoryPlatformService service) {
        this.service = service;
    }

    @GetMapping
    public List<Event> getPublishedEvents() {
        return service.getPublishedEvents();
    }

    @PostMapping("/{clubId}")
    public Event createEvent(@PathVariable Long clubId, @RequestBody dto.EventRequest request, @RequestHeader(name = "Authorization", required = false) String authHeader) {
        return service.createEvent(clubId, request, service.extractEmail(authHeader));
    }

    @GetMapping("/my")
    public List<Event> getMyEvents(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        return service.getMyEvents(service.extractEmail(authHeader));
    }

    @PutMapping("/{eventId}/publish")
    public ResponseEntity<Void> publish(@PathVariable Long eventId, @RequestHeader(name = "Authorization", required = false) String authHeader) {
        service.publishEvent(eventId, service.extractEmail(authHeader));
        return ResponseEntity.ok().build();
    }
}
