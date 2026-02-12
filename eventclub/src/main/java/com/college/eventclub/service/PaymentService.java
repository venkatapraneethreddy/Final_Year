package com.college.eventclub.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.college.eventclub.model.Event;
import com.college.eventclub.model.EventRegistration;
import com.college.eventclub.model.Payment;
import com.college.eventclub.model.PaymentStatus;
import com.college.eventclub.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment processPayment(EventRegistration registration) {

        Event event = registration.getEvent();

        if (!event.isPaid()) {
            throw new RuntimeException("Event is free");
        }

        Payment payment = new Payment();
        payment.setRegistration(registration);
        payment.setAmount(event.getFee());
        payment.setStatus(PaymentStatus.SUCCESS); // mock success
        payment.setPaymentTime(LocalDateTime.now());

        return paymentRepository.save(payment);
    }
}
