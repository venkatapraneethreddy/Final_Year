package com.college.eventclub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.college.eventclub.model.Club;
import com.college.eventclub.service.ClubService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ClubService clubService;

    public AdminController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PutMapping("/clubs/{id}/approve")
    public ResponseEntity<Club> approveClub(@PathVariable Long id,
                                            Authentication authentication) {

        // role from JWT
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        Club approvedClub = clubService.approveClub(id);
        return ResponseEntity.ok(approvedClub);
    }
}
