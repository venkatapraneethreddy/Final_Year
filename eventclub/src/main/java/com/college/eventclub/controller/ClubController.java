package com.college.eventclub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.college.eventclub.model.Club;
import com.college.eventclub.model.User;
import com.college.eventclub.service.ClubService;
import com.college.eventclub.service.UserService;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubService clubService;
    private final UserService userService;

    public ClubController(ClubService clubService,
                          UserService userService) {
        this.clubService = clubService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Club> createClub(@RequestBody Club club,
                                           Authentication authentication) {

        // email extracted from JWT
        String email = authentication.getName();

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        club.setCreatedBy(user);

        Club savedClub = clubService.createClub(club);
        return new ResponseEntity<>(savedClub, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<?> getApprovedClubs() {
        return ResponseEntity.ok(clubService.getApprovedClubs());
}

}
