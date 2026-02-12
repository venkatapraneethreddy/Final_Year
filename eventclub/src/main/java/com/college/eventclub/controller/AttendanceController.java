package com.college.eventclub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.college.eventclub.model.EventRegistration;
import com.college.eventclub.service.AttendanceService;
import com.college.eventclub.service.EventRegistrationService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EventRegistrationService registrationService;

    public AttendanceController(AttendanceService attendanceService,
                                EventRegistrationService registrationService) {
        this.attendanceService = attendanceService;
        this.registrationService = registrationService;
    }

    @PostMapping("/scan")
    public ResponseEntity<?> scanQr(@RequestParam String qrCode) {

        EventRegistration registration =
                registrationService.findByQrCode(qrCode)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid QR"));

        return new ResponseEntity<>(
                attendanceService.markAttendance(registration),
                HttpStatus.CREATED
        );
    }
}
