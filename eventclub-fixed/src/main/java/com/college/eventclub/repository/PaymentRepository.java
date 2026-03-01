package com.college.eventclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.college.eventclub.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
