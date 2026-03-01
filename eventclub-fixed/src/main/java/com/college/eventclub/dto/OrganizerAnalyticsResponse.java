package com.college.eventclub.dto;

import java.util.List;

public class OrganizerAnalyticsResponse {

    private long totalEvents;
    private long totalRegistrations;
    private List<EventStat> eventStats;

    public OrganizerAnalyticsResponse(
            long totalEvents,
            long totalRegistrations,
            List<EventStat> eventStats) {
        this.totalEvents = totalEvents;
        this.totalRegistrations = totalRegistrations;
        this.eventStats = eventStats;
    }

    public long getTotalEvents() { return totalEvents; }
    public long getTotalRegistrations() { return totalRegistrations; }
    public List<EventStat> getEventStats() { return eventStats; }

    public static class EventStat {
        private String eventTitle;
        private long registrations;

        public EventStat(String eventTitle, long registrations) {
            this.eventTitle = eventTitle;
            this.registrations = registrations;
        }

        public String getEventTitle() { return eventTitle; }
        public long getRegistrations() { return registrations; }
    }
}