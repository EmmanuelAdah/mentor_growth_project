package com.server.mentorgrowth.services.interfaces;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.requests.VerifyPaymentRequest;
import com.server.mentorgrowth.dtos.response.InitiatePaymentResponse;
import com.server.mentorgrowth.dtos.response.PaymentResponse;

import java.util.List;

public interface PaymentService {

    InitiatePaymentResponse createPayment(PaymentRequest paymentRequest);
    PaymentResponse verifyPayment(VerifyPaymentRequest verifyPaymentRequest);
    PaymentResponse findById(String id);
    List<PaymentResponse> findByUserId(String userId);
    PaymentResponse findByReference(String reference);
    List<PaymentResponse> findAll();
}
