package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.requests.VerifyPaymentRequest;
import com.server.mentorgrowth.dtos.response.InitiatePaymentResponse;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.services.interfaces.PaymentService;
import com.server.mentorgrowth.services.PaystackWebhookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final PaystackWebhookService webhookService;

    @Value("${paystack.apiKey}")
    private String paystackSecret;

    @PostMapping("/create")
    public ResponseEntity<InitiatePaymentResponse> createPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(paymentRequest));
    }

    @PutMapping("/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(@Valid @RequestBody VerifyPaymentRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.verifyPayment(request));
    }

    @GetMapping("/id")
    public ResponseEntity<PaymentResponse> findById(@RequestParam String id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>> findByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(paymentService.findByUserId(userId));
    }

    @GetMapping("/get/payment/{reference}")
    public ResponseEntity<PaymentResponse> findByReference(@PathVariable String reference) {
        return ResponseEntity.ok(paymentService.findByReference(reference));
    }

    @GetMapping("/get/payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping("/{paymentId}/receipt")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable String paymentId,
                                                  @RequestParam String userId) {
        byte[] pdf = paymentService.generatePaymentReceipt(paymentId, userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt-" + paymentId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/paystack")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("x-paystack-signature") String signature,
            @RequestBody String payload) {

        if (!webhookService.verifySignature(payload, signature, paystackSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        }

        webhookService.processEvent(payload);
        return ResponseEntity.ok("Webhook received");
    }
}
