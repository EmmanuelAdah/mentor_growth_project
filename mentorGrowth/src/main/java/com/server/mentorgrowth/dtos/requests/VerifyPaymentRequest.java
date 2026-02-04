package com.server.mentorgrowth.dtos.requests;

import lombok.Data;

@Data
public class VerifyPaymentRequest {
    private String userId;
    private String paymentReference;
}
