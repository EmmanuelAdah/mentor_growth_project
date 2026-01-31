package com.server.mentorgrowth.dtos.response;

public record InitiatePaymentResponse(
        String authorizationUrl,
        String reference
) { }
