package com.college.eventclub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.college.eventclub.model.User;
import com.college.eventclub.model.Club;
import com.college.eventclub.model.Event;
import com.college.eventclub.model.EventStatus;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByEventIdAndStatus(Long id, EventStatus status);
    List<Event> findByClub(Club club);
}