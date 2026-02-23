package com.college.eventclub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

import com.college.eventclub.config.JwtUtil;
import com.college.eventclub.dto.LoginRequest;
import com.college.eventclub.model.Role;
import com.college.eventclub.model.User;
import com.college.eventclub.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
public ResponseEntity<User> registerUser(@RequestBody User user) {

    if (user.getRole() == null) {
        user.setRole(Role.STUDENT);
    }

    // ✅ ENCODE PASSWORD
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    User savedUser = userService.saveUser(user);
    return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
}
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {

    return userService.findByEmail(request.getEmail())
        .map(user -> {
            if (passwordEncoder.matches(
                    request.getPassword(), user.getPassword())) {

                String token = JwtUtil.generateToken(
                        user.getEmail(),
                        user.getRole().name());

                return ResponseEntity.ok(
                    Map.of(
                        "token", token,
                        "role", user.getRole().name()
                    )
                );

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid credentials");
            }
        })
        .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User not found"));
}


}