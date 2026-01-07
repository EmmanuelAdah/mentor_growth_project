package com.server.mentorgrowth.dtos.requests;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private String menteeId;
    private String mentorId;
    private long amount;
    private String currency;
}
