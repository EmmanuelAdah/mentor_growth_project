package com.server.mentorgrowth.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String mentorId;

    @NotNull
    @Positive
    private long amount;

    @NotNull
    private String currency;
}
