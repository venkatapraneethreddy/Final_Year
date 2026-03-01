package com.college.eventclub.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.college.eventclub.model.Payment;
import com.college.eventclub.model.PaymentStatus;
import com.college.eventclub.model.EventRegistration;
import com.college.eventclub.model.RegistrationStatus;
import com.college.eventclub.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final EventRegistrationService registrationService;

    public PaymentService(PaymentRepository paymentRepository,
                          EventRegistrationService registrationService) {
        this.paymentRepository = paymentRepository;
        this.registrationService = registrationService;
    }

    public Payment processPayment(EventRegistration registration) {

        // Ensure event is actually paid
        if (!registration.getEvent().isPaid()) {
            throw new RuntimeException("This event does not require payment");
        }

        // Ensure not already paid
        if (registration.getStatus() != RegistrationStatus.PENDING_PAYMENT) {
            throw new RuntimeException("Payment already completed or not required");
        }

        Payment payment = new Payment();
        payment.setRegistration(registration);
        payment.setAmount(registration.getEvent().getFee());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentTime(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        // Feature 4: confirm the registration after payment succeeds
        registrationService.confirmPayment(registration.getRegistrationId());

        return saved;
    }
}
