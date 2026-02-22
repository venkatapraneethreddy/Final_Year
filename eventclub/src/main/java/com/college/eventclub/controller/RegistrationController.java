package com.college.eventclub.controller;

import com.college.eventclub.model.Registration;
import com.college.eventclub.service.InMemoryPlatformService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
    private final InMemoryPlatformService service;

    public RegistrationController(InMemoryPlatformService service) {
        this.service = service;
    }

    @PostMapping("/{eventId}")
    public Registration register(@PathVariable Long eventId, @RequestHeader(name = "Authorization", required = false) String authHeader) {
        return service.registerForEvent(eventId, service.extractEmail(authHeader));
    }

    @GetMapping("/my")
    public List<Registration> myRegistrations(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        return service.getMyRegistrations(service.extractEmail(authHeader));
    }

    @PutMapping("/{eventId}/attendance")
    public void markAttendance(@PathVariable Long eventId, @RequestHeader(name = "Authorization", required = false) String authHeader) {
        service.markAttendance(eventId, service.extractEmail(authHeader));
    }
}
