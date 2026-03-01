package com.college.eventclub.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.college.eventclub.dto.OrganizerAnalyticsResponse;
import com.college.eventclub.model.Club;
import com.college.eventclub.model.Event;
import com.college.eventclub.model.EventStatus;
import com.college.eventclub.model.User;
import com.college.eventclub.repository.ClubRepository;
import com.college.eventclub.repository.EventRegistrationRepository;
import com.college.eventclub.repository.EventRepository;
import com.college.eventclub.repository.UserRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final EventRegistrationRepository eventRegistrationRepository;

    public EventService(EventRepository eventRepository,
                        UserRepository userRepository,
                        ClubRepository clubRepository,
                        EventRegistrationRepository eventRegistrationRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.eventRegistrationRepository = eventRegistrationRepository;
    }

    // FIX: Added organizerEmail param - verify organizer owns the club
    public Event createEventForClub(Event event, Club club, String organizerEmail) {
        if (!club.getCreatedBy().getEmail().equals(organizerEmail)) {
            throw new RuntimeException("You can only post events for your own club");
        }
        event.setClub(club);
        event.setStatus(EventStatus.DRAFT);
        return eventRepository.save(event);
    }

    // FIX: Organizer can only publish their own events
    public Event publishEvent(Long eventId, String organizerEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getClub().getCreatedBy().getEmail().equals(organizerEmail)) {
            throw new RuntimeException("You can only publish your own events");
        }

        event.setStatus(EventStatus.PUBLISHED);
        return eventRepository.save(event);
    }

    // Admin-only version - no ownership check needed
    public Event adminPublishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(EventStatus.PUBLISHED);
        return eventRepository.save(event);
    }

    // FIX: Only return PUBLISHED events (was returning ALL events)
    public List<Event> getPublishedEvents() {
        return eventRepository.findByStatus(EventStatus.PUBLISHED);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public List<Event> getEventsByOrganizer(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Club club = clubRepository.findByCreatedBy(user)
                .orElseThrow(() -> new RuntimeException("Club not found for this organizer"));

        return eventRepository.findByClub(club);
    }

    // NEW: Update event (organizer can only update their own)
    public Event updateEvent(Long eventId, Event updates, String organizerEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getClub().getCreatedBy().getEmail().equals(organizerEmail)) {
            throw new RuntimeException("You can only update your own events");
        }

        if (updates.getTitle() != null) event.setTitle(updates.getTitle());
        if (updates.getDescription() != null) event.setDescription(updates.getDescription());
        if (updates.getEventDate() != null) event.setEventDate(updates.getEventDate());
        if (updates.getLocation() != null) event.setLocation(updates.getLocation());
        if (updates.getCapacity() != null) event.setCapacity(updates.getCapacity());
        if (updates.getFee() != null) event.setFee(updates.getFee());
        event.setPaid(updates.isPaid());

        return eventRepository.save(event);
    }

    // NEW: Cancel/delete event
    public void cancelEvent(Long eventId, String organizerEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getClub().getCreatedBy().getEmail().equals(organizerEmail)) {
            throw new RuntimeException("You can only cancel your own events");
        }

        event.setStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
    }

    public OrganizerAnalyticsResponse getOrganizerAnalytics(String organizerEmail) {
        var events = eventRepository.findByClub_CreatedBy_Email(organizerEmail);

        long totalEvents = events.size();
        long totalRegistrations = 0;

        List<OrganizerAnalyticsResponse.EventStat> stats = new ArrayList<>();

        for (var event : events) {
            long count = eventRegistrationRepository.countByEvent_EventId(event.getEventId());
            totalRegistrations += count;
            stats.add(new OrganizerAnalyticsResponse.EventStat(event.getTitle(), count));
        }

        return new OrganizerAnalyticsResponse(totalEvents, totalRegistrations, stats);
    }
}
