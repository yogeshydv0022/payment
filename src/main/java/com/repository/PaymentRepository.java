package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findByRazorpayOrderId(String razorpayOrderId);
}
