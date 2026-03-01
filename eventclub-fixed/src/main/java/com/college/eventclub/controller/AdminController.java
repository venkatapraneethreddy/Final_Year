package com.college.eventclub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.college.eventclub.model.Club;
import com.college.eventclub.service.AdminService;
import com.college.eventclub.service.ClubService;
import com.college.eventclub.service.EventService;
import com.college.eventclub.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ClubService clubService;
    private final AdminService adminService;
    private final EventService eventService;
    private final UserService userService;

    public AdminController(ClubService clubService,
                           AdminService adminService,
                           EventService eventService,
                           UserService userService) {
        this.clubService = clubService;
        this.adminService = adminService;
        this.eventService = eventService;
        this.userService = userService;
    }

    @PutMapping("/clubs/{id}/approve")
    public ResponseEntity<Club> approveClub(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.approveClub(id));
    }

    @PutMapping("/clubs/{id}/reject")
    public ResponseEntity<Club> rejectClub(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.rejectClub(id));
    }

    @GetMapping("/clubs/pending")
    public ResponseEntity<?> getPendingClubs() {
        return ResponseEntity.ok(clubService.getPendingClubs());
    }

    @GetMapping("/events")
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PutMapping("/events/{eventId}/publish")
    public ResponseEntity<?> publishEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.adminPublishEvent(eventId));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    // Feature 6: user management
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
