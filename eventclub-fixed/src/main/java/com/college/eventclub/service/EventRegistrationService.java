package com.college.eventclub.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.college.eventclub.model.Event;
import com.college.eventclub.model.EventRegistration;
import com.college.eventclub.model.EventStatus;
import com.college.eventclub.model.RegistrationStatus;
import com.college.eventclub.model.User;
import com.college.eventclub.repository.EventRegistrationRepository;
import com.college.eventclub.repository.UserRepository;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository eventRegistrationRepository;
    private final UserRepository userRepository;
	private final EventService eventService;


public EventRegistrationService(
        EventRegistrationRepository eventRegistrationRepository,
        UserRepository userRepository,
        EventService eventService) {
    this.eventRegistrationRepository = eventRegistrationRepository;
    this.userRepository = userRepository;
    this.eventService = eventService;
}

    public EventRegistration register(User user, Event event) {

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new RuntimeException("Event is not open for registration");
        }

        eventRegistrationRepository.findByEventAndUser(event, user)
                .ifPresent(r -> {
                    throw new RuntimeException("Already registered for this event");
                });

        // Capacity check
        if (event.getCapacity() != null) {
            long currentCount = eventRegistrationRepository.countByEvent(event);
            if (currentCount >= event.getCapacity()) {
                throw new RuntimeException("Event is full - no spots remaining");
            }
        }

        EventRegistration registration = new EventRegistration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegisteredAt(LocalDateTime.now());
        registration.setQrCode(UUID.randomUUID().toString());

        // Feature 4: free events are immediately CONFIRMED, paid events start as PENDING_PAYMENT
        if (event.isPaid()) {
            registration.setStatus(RegistrationStatus.PENDING_PAYMENT);
        } else {
            registration.setStatus(RegistrationStatus.CONFIRMED);
        }

        return eventRegistrationRepository.save(registration);
    }

    // Called by PaymentService after successful payment
    public EventRegistration confirmPayment(Long registrationId) {
        EventRegistration registration = eventRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        if (registration.getStatus() != RegistrationStatus.PENDING_PAYMENT) {
            throw new RuntimeException("Registration is not awaiting payment");
        }

        registration.setStatus(RegistrationStatus.CONFIRMED);
        return eventRegistrationRepository.save(registration);
    }

    public Optional<EventRegistration> findByQrCode(String qrCode) {
        return eventRegistrationRepository.findByQrCode(qrCode);
    }

    public Optional<EventRegistration> findById(Long id) {
        return eventRegistrationRepository.findById(id);
    }

    public List<EventRegistration> getRegistrationsByStudent(String email) {
        User student = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return eventRegistrationRepository.findByUser(student);
    }

    public void cancelRegistration(Long registrationId, String userEmail) {
        EventRegistration registration = eventRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        if (!registration.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only cancel your own registrations");
        }

        eventRegistrationRepository.delete(registration);
    }
	
	public List<EventRegistration> getRegistrantsForEvent(Long eventId, String organizerEmail) {

    Event event = eventService.getEventById(eventId);

    // ✅ Correct field: createdBy
    if (!event.getClub().getCreatedBy().getEmail().equals(organizerEmail)) {
        throw new RuntimeException("You are not authorized to view these registrants");
    }

    return eventRegistrationRepository.findByEvent(event);
}
}
