package com.server.mentorgrowth.dtos.response;

import lombok.Data;

@Data
public class PaymentResponse {
    private String Id;
    private String userId;
    private String mentorId;
    private String status;
    private long amount;
    private String currency;
}
