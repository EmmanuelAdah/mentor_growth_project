package com.server.mentorgrowth.dtos.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private String Id;
    private String userId;
    private String mentorId;
    private double amount;
    private String currency;
    private String status;
    private String reference;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
