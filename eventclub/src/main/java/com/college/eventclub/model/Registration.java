package com.college.eventclub.model;

public class Registration {
    private Long registrationId;
    private Event event;
    private String studentEmail;
    private String paymentStatus;
    private double paidAmount;
    private boolean attendanceMarked;

    public Registration(Long registrationId, Event event, String studentEmail, String paymentStatus, double paidAmount, boolean attendanceMarked) {
        this.registrationId = registrationId;
        this.event = event;
        this.studentEmail = studentEmail;
        this.paymentStatus = paymentStatus;
        this.paidAmount = paidAmount;
        this.attendanceMarked = attendanceMarked;
    }

    public Long getRegistrationId() { return registrationId; }
    public Event getEvent() { return event; }
    public String getStudentEmail() { return studentEmail; }
    public String getPaymentStatus() { return paymentStatus; }
    public double getPaidAmount() { return paidAmount; }
    public boolean isAttendanceMarked() { return attendanceMarked; }
    public void setAttendanceMarked(boolean attendanceMarked) { this.attendanceMarked = attendanceMarked; }
}
