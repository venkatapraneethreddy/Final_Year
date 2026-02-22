package com.college.eventclub.controller;

import com.college.eventclub.model.Club;
import com.college.eventclub.service.InMemoryPlatformService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final InMemoryPlatformService service;

    public AdminController(InMemoryPlatformService service) {
        this.service = service;
    }

    @GetMapping("/clubs/pending")
    public List<Club> pendingClubs() {
        return service.getPendingClubs();
    }

    @PutMapping("/clubs/{clubId}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long clubId) {
        service.approveClub(clubId);
        return ResponseEntity.ok().build();
    }
}
