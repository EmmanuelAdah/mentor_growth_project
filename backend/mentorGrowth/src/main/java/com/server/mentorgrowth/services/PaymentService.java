package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest paymentRequest);
}
