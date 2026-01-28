package com.server.mentorgrowth.dtos.response;

import com.server.mentorgrowth.models.Mentee;
import com.server.mentorgrowth.models.Mentor;
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
