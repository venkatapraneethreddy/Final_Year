package com.college.eventclub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.college.eventclub.dto.UserResponse;
import com.college.eventclub.model.User;
import com.college.eventclub.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Feature 6: admin fetches all users (safe DTO, no passwords)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();
    }

    // Feature 6: admin deletes a user
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }
}
