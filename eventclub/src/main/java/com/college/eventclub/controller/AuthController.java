package com.college.eventclub.controller;

import com.college.eventclub.model.dto;
import com.college.eventclub.service.InMemoryPlatformService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final InMemoryPlatformService service;

    public AuthController(InMemoryPlatformService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public dto.AuthResponse login(@RequestBody dto.AuthRequest request) {
        return service.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody dto.RegisterRequest request) {
        service.register(request);
        return ResponseEntity.ok().build();
    }
}
