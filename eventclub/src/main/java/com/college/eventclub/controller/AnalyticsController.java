package com.college.eventclub.controller;

import com.college.eventclub.model.dto;
import com.college.eventclub.service.InMemoryPlatformService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final InMemoryPlatformService service;

    public AnalyticsController(InMemoryPlatformService service) {
        this.service = service;
    }

    @GetMapping("/events/{eventId}")
    public dto.EventStats eventStats(@PathVariable Long eventId) {
        return service.getStats(eventId);
    }
}
