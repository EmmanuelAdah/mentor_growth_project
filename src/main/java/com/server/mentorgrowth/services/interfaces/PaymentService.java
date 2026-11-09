package com.server.mentorgrowth.services.interfaces;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.requests.VerifyPaymentRequest;
import com.server.mentorgrowth.dtos.response.InitiatePaymentResponse;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.models.Payment;

import java.util.List;

public interface PaymentService {

    InitiatePaymentResponse createPayment(PaymentRequest paymentRequest, String currentUserId);
    PaymentResponse verifyPayment(VerifyPaymentRequest verifyPaymentRequest, String currentUserId);
    PaymentResponse findById(String id, String currentUserId);
    PaymentResponse findByReference(String reference, String currentUserId);
    List<PaymentResponse> findByUserId(String userId);
    List<PaymentResponse> findAll();
    byte[] generatePaymentReceipt(String paymentId, String userId);
}
