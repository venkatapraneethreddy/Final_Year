package com.college.eventclub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.college.eventclub.model.Event;
import com.college.eventclub.model.EventRegistration;
import com.college.eventclub.model.User;

public interface EventRegistrationRepository
        extends JpaRepository<EventRegistration, Long> {
        
        Optional<EventRegistration> findByEventAndUser(Event event, User user);
        long countByEvent(Event event);
        List<EventRegistration> findByUser(User user);


}
