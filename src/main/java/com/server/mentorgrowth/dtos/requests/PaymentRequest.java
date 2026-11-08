package com.server.mentorgrowth.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotBlank
    private String mentorId;

    @NotNull
    @Positive
    private double amount;

    @NotBlank
    @Pattern(regexp = "USD|NGN|GBP", message = "Currency must be USD, NGN, or GBP")
    private String currency;
}
