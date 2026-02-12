package com.college.eventclub.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.college.eventclub.model.Event;
import com.college.eventclub.model.EventRegistration;
import com.college.eventclub.model.EventStatus;
import com.college.eventclub.model.User;
import com.college.eventclub.repository.EventRegistrationRepository;
import com.college.eventclub.repository.UserRepository;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository eventRegistrationRepository;
    private final UserRepository userRepository;

    public EventRegistrationService(EventRegistrationRepository eventRegistrationRepository,
                                    UserRepository userRepository) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.userRepository = userRepository;
    }

    public EventRegistration register(User user, Event event) {

    if (event.getStatus() != EventStatus.PUBLISHED) {
        throw new RuntimeException("Event is not open for registration");
    }

    eventRegistrationRepository.findByEventAndUser(event, user)
            .ifPresent(r -> {
                throw new RuntimeException("Already registered");
            });

    EventRegistration registration = new EventRegistration();
    registration.setUser(user);
    registration.setEvent(event);
    registration.setRegisteredAt(LocalDateTime.now());
    registration.setQrCode(UUID.randomUUID().toString());
    return eventRegistrationRepository.save(registration);
}

public Optional<EventRegistration> findByQrCode(String qrCode) {
    return eventRegistrationRepository.findAll()
            .stream()
            .filter(r -> qrCode.equals(r.getQrCode()))
            .findFirst();
}
public Optional<EventRegistration> findById(Long id) {
    return eventRegistrationRepository.findById(id);
}
public List<EventRegistration> getRegistrationsByStudent(String email) {

    User student = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return eventRegistrationRepository.findByUser(student);
}


}
