package com.college.eventclub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.college.eventclub.model.Event;
import com.college.eventclub.model.EventRegistration;
import com.college.eventclub.model.User;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {

    Optional<EventRegistration> findByEventAndUser(Event event, User user);

    long countByEvent(Event event);

    long countByEvent_EventId(Long eventId);

    List<EventRegistration> findByUser(User user);

    Optional<EventRegistration> findByQrCode(String qrCode);

    // Feature 5: fetch all registrations for a specific event
    List<EventRegistration> findByEvent(Event event);

    // Feature 5: fetch registrations for any event in a club
    List<EventRegistration> findByEvent_Club_CreatedBy_Email(String email);
}
