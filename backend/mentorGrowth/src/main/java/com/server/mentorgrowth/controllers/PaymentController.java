package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.requests.VerifyPaymentRequest;
import com.server.mentorgrowth.dtos.response.InitiatePaymentResponse;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.models.Payment;
import com.server.mentorgrowth.services.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentServiceImpl paymentService;

    @PostMapping("/create/payment")
    public ResponseEntity<InitiatePaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest) {
        return new ResponseEntity<>(paymentService.createPayment(paymentRequest), HttpStatus.CREATED);
    }

    @PatchMapping("/verify/payment")
    public ResponseEntity<PaymentResponse> verifyPayment(@RequestBody VerifyPaymentRequest request) {
        return new ResponseEntity<>(paymentService.verifyPayment(request), HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<PaymentResponse> findById(@RequestParam String id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @GetMapping("/get/payment/{reference}")
    public ResponseEntity<PaymentResponse> findByReference(@PathVariable String reference) {
        return ResponseEntity.ok(paymentService.findByReference(reference));
    }

    @GetMapping("/get/payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.findAll());
    }
}
