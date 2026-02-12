package com.college.eventclub.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.college.eventclub.model.Attendance;
import com.college.eventclub.model.EventRegistration;
import com.college.eventclub.repository.AttendanceRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public Attendance markAttendance(EventRegistration registration) {

        attendanceRepository.findByRegistration(registration)
                .ifPresent(a -> {
                    throw new RuntimeException("Attendance already marked");
                });

        Attendance attendance = new Attendance();
        attendance.setRegistration(registration);
        attendance.setCheckInTime(LocalDateTime.now());

        return attendanceRepository.save(attendance);
    }
}
