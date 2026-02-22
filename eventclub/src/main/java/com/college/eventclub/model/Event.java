package com.college.eventclub.model;

public class Event {
    private Long eventId;
    private Long clubId;
    private String organizerEmail;
    private String title;
    private String description;
    private String location;
    private boolean paid;
    private double fee;
    private EventStatus status;

    public Event(Long eventId, Long clubId, String organizerEmail, String title, String description, String location, boolean paid, double fee, EventStatus status) {
        this.eventId = eventId;
        this.clubId = clubId;
        this.organizerEmail = organizerEmail;
        this.title = title;
        this.description = description;
        this.location = location;
        this.paid = paid;
        this.fee = fee;
        this.status = status;
    }

    public Long getEventId() { return eventId; }
    public Long getClubId() { return clubId; }
    public String getOrganizerEmail() { return organizerEmail; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public boolean isPaid() { return paid; }
    public double getFee() { return fee; }
    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { this.status = status; }
}
