package com.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.Payment;
import com.razorpay.RazorpayException;
import com.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createPayment(@RequestBody Payment payment) {
        try {
            Payment createdPayment = paymentService.createPayment(payment);
            return ResponseEntity.ok(createdPayment);
        } catch (RazorpayException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Payment creation failed");
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<?> handlePaymentCallback(@RequestBody Map<String, String> responsePayload) {
        String razorpayOrderId = responsePayload.get("razorpay_order_id");
        Payment updatedPayment = paymentService.handlePaymentCallback(razorpayOrderId);
        return updatedPayment != null ? ResponseEntity.ok(updatedPayment) : ResponseEntity.status(404).body("Payment not found");
    }
}
