package com.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Payment;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.repository.PaymentRepository;

@Service
public class PaymentService {

    

    private final PaymentRepository paymentRepository;
    
    @Autowired
	private RazorpayClient client;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Payment payment) throws RazorpayException {
     
        JSONObject json = new JSONObject();
        json.put("amount", payment.getAmount() * 100); // amount in paisa
        json.put("currency", "INR");
        json.put("receipt", payment.getEmail());

        Order razorpayOrder = client.orders.create(json);

        payment.setRazorpayOrderId(razorpayOrder.get("id"));
        payment.setStatus(razorpayOrder.get("status"));

        return paymentRepository.save(payment);
    }

    public Payment handlePaymentCallback(String razorpayOrderId) {
        Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId);
        if (payment != null) {
            payment.setStatus("PAYMENT_COMPLETED");
            return paymentRepository.save(payment);
        }
        return null;
    }
}
