package com.college.eventclub.service;

import com.college.eventclub.model.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryPlatformService {
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();
    private final Map<Long, Club> clubs = new ConcurrentHashMap<>();
    private final Map<Long, Event> events = new ConcurrentHashMap<>();
    private final Map<Long, Registration> registrations = new ConcurrentHashMap<>();

    private final AtomicLong userId = new AtomicLong(4);
    private final AtomicLong eventId = new AtomicLong(3);
    private final AtomicLong registrationId = new AtomicLong(1);

    public InMemoryPlatformService() {
        usersByEmail.put("admin@eventclub.com", new User(1L, "Admin User", "admin@eventclub.com", "admin123", Role.ADMIN));
        usersByEmail.put("organizer@eventclub.com", new User(2L, "Organizer User", "organizer@eventclub.com", "organizer123", Role.ORGANIZER));
        usersByEmail.put("student@eventclub.com", new User(3L, "Student User", "student@eventclub.com", "student123", Role.STUDENT));

        clubs.put(1L, new Club(1L, "Tech Innovators", "Club for hackathons and coding events", "APPROVED"));
        clubs.put(2L, new Club(2L, "Cultural Crew", "Music, dance and theater", "PENDING"));

        events.put(1L, new Event(1L, 1L, "organizer@eventclub.com", "AI Bootcamp", "Hands-on AI workshop", "Auditorium", true, 299, EventStatus.PUBLISHED));
        events.put(2L, new Event(2L, 1L, "organizer@eventclub.com", "Open Source Sprint", "Collaborative OSS contributions", "Lab 2", false, 0, EventStatus.DRAFT));
    }

    public dto.AuthResponse login(dto.AuthRequest request) {
        User user = usersByEmail.get(request.email());
        if (user == null || !user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = Base64.getEncoder().encodeToString((user.getEmail() + ":" + user.getRole()).getBytes(StandardCharsets.UTF_8));
        return new dto.AuthResponse(token, user.getRole().name());
    }

    public void register(dto.RegisterRequest request) {
        if (usersByEmail.containsKey(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Role role = request.role() == null || request.role().isBlank() ? Role.STUDENT : Role.valueOf(request.role().toUpperCase(Locale.ROOT));
        usersByEmail.put(request.email(), new User(userId.getAndIncrement(), request.fullName(), request.email(), request.password(), role));
    }

    public String extractEmail(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String decoded = new String(Base64.getDecoder().decode(authHeader.substring(7)), StandardCharsets.UTF_8);
        return decoded.split(":")[0];
    }

    public List<Event> getPublishedEvents() {
        return events.values().stream().filter(e -> e.getStatus() == EventStatus.PUBLISHED).sorted(Comparator.comparing(Event::getEventId)).toList();
    }

    public Event createEvent(Long clubId, dto.EventRequest request, String organizerEmail) {
        if (!clubs.containsKey(clubId) || !"APPROVED".equalsIgnoreCase(clubs.get(clubId).getStatus())) {
            throw new IllegalArgumentException("Club not approved");
        }
        Event event = new Event(eventId.getAndIncrement(), clubId, organizerEmail, request.title(), request.description(), request.location(), request.paid(), request.paid() ? request.fee() : 0, EventStatus.DRAFT);
        events.put(event.getEventId(), event);
        return event;
    }

    public List<Event> getMyEvents(String email) {
        return events.values().stream().filter(e -> e.getOrganizerEmail().equals(email)).sorted(Comparator.comparing(Event::getEventId)).toList();
    }

    public void publishEvent(Long eventId, String email) {
        Event event = events.get(eventId);
        if (event == null || !event.getOrganizerEmail().equals(email)) {
            throw new IllegalArgumentException("Event not found");
        }
        event.setStatus(EventStatus.PUBLISHED);
    }

    public Registration registerForEvent(Long eventId, String studentEmail) {
        Event event = events.get(eventId);
        if (event == null || event.getStatus() != EventStatus.PUBLISHED) {
            throw new IllegalArgumentException("Event unavailable");
        }
        boolean alreadyExists = registrations.values().stream().anyMatch(r -> r.getEvent().getEventId().equals(eventId) && r.getStudentEmail().equals(studentEmail));
        if (alreadyExists) {
            throw new IllegalArgumentException("Already registered");
        }
        String paymentStatus = event.isPaid() ? "PAID" : "FREE";
        Registration registration = new Registration(registrationId.getAndIncrement(), event, studentEmail, paymentStatus, event.isPaid() ? event.getFee() : 0, false);
        registrations.put(registration.getRegistrationId(), registration);
        return registration;
    }

    public List<Registration> getMyRegistrations(String studentEmail) {
        return registrations.values().stream().filter(r -> r.getStudentEmail().equals(studentEmail)).sorted(Comparator.comparing(Registration::getRegistrationId)).toList();
    }

    public List<Club> getPendingClubs() {
        return clubs.values().stream().filter(c -> "PENDING".equalsIgnoreCase(c.getStatus())).toList();
    }

    public void approveClub(Long clubId) {
        Club club = clubs.get(clubId);
        if (club == null) {
            throw new IllegalArgumentException("Club not found");
        }
        club.setStatus("APPROVED");
    }

    public dto.EventStats getStats(Long eventId) {
        List<Registration> eventRegs = registrations.values().stream().filter(r -> r.getEvent().getEventId().equals(eventId)).toList();
        long total = eventRegs.size();
        long paid = eventRegs.stream().filter(r -> "PAID".equals(r.getPaymentStatus())).count();
        long attendance = eventRegs.stream().filter(Registration::isAttendanceMarked).count();
        double revenue = eventRegs.stream().mapToDouble(Registration::getPaidAmount).sum();
        return new dto.EventStats(total, paid, attendance, revenue);
    }

    public void markAttendance(Long eventId, String studentEmail) {
        registrations.values().stream()
                .filter(r -> r.getEvent().getEventId().equals(eventId) && r.getStudentEmail().equals(studentEmail))
                .findFirst()
                .ifPresent(r -> r.setAttendanceMarked(true));
    }
}
