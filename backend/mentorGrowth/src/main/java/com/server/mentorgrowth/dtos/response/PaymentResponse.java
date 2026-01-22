package com.server.mentorgrowth.dtos.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private String Id;
    private String userId;
    private String mentorId;
    private String status;
    private double amount;
    private String currency;

    private LocalDateTime createdAt;
}
