package com.college.eventclub.model;

public class dto {
    public record AuthRequest(String email, String password) {}
    public record RegisterRequest(String fullName, String email, String password, String role) {}
    public record AuthResponse(String token, String role) {}
    public record EventRequest(String title, String description, String location, boolean paid, double fee) {}
    public record EventStats(long totalRegistrations, long paidRegistrations, long attendanceCount, double revenue) {}
}
