package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.response.InitiatePaymentResponse;
import com.server.mentorgrowth.dtos.response.PaymentResponse;

public interface PaymentService {

    InitiatePaymentResponse createPayment(PaymentRequest paymentRequest);
    PaymentResponse verifyPayment(String userId, String paymentReference);
}
