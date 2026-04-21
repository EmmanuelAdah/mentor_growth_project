package com.server.mentorgrowth.dtos.response;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class PaymentResponse {
    private String Id;
    private String userId;
    private String mentorId;
    private double amount;
    private String currency;
    private String status;
    private String reference;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
