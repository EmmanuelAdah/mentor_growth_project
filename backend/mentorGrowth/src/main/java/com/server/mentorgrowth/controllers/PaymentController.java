package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.response.InitiatePaymentResponse;
import com.server.mentorgrowth.services.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentServiceImpl paymentService;

    @PostMapping("/mentee/payment")
    public ResponseEntity<InitiatePaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest) {
        return new ResponseEntity<>(paymentService.createPayment(paymentRequest), HttpStatus.OK);
    }


}
