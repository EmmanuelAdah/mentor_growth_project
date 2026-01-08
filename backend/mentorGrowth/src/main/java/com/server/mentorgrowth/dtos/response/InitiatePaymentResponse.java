package com.server.mentorgrowth.dtos.response;

import lombok.Data;

@Data
public class InitiatePaymentResponse {
    private String authorizationUrl;
    private String reference;
}
