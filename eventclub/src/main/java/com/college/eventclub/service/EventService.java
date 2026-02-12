package com.college.eventclub.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.college.eventclub.model.Club;
import com.college.eventclub.model.Event;
import com.college.eventclub.model.EventStatus;
import com.college.eventclub.model.User;
import com.college.eventclub.repository.ClubRepository;
import com.college.eventclub.repository.EventRepository;
import com.college.eventclub.repository.UserRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public EventService(EventRepository eventRepository,
                        UserRepository userRepository,
                        ClubRepository clubRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }

    public Event createEvent(Event event) {
        event.setStatus(EventStatus.DRAFT);
        return eventRepository.save(event);
    }

    public Event createEventForClub(Event event, Club club) {
        event.setClub(club);
        event.setStatus(EventStatus.DRAFT);
        return eventRepository.save(event);
    }
    public Event publishEvent(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setStatus(EventStatus.PUBLISHED);
        return eventRepository.save(event);
    }

    public List<Event> getPublishedEvents() {
        return eventRepository.findAll()
            .stream()
            .filter(event -> event.getStatus() == EventStatus.PUBLISHED)
            .toList();
    }
    public List<Event> getAllEvents() {
    return eventRepository.findAll();
}
public List<Event> getEventsByOrganizer(String email) {

    // find user
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // find club created by this user
    Club club = clubRepository.findByCreatedBy(user)
            .orElseThrow(() -> new RuntimeException("Club not found"));

    // return events of that club
    return eventRepository.findByClub(club);
}
public Event getEventById(Long eventId) {
    return eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));
}


}
