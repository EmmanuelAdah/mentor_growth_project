//package com.server.mentorgrowth.controllers;
//
//import com.server.mentorgrowth.dtos.requests.PaymentRequest;
//import com.server.mentorgrowth.dtos.requests.VerifyPaymentRequest;
//import com.server.mentorgrowth.dtos.response.InitiatePaymentResponse;
//import com.server.mentorgrowth.dtos.response.PaymentResponse;
//import com.server.mentorgrowth.services.PaymentServiceImpl;
//import com.server.mentorgrowth.services.PaystackWebhookService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import java.util.List;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//
//class PaymentControllerTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @MockitoBean
//    private PaymentServiceImpl paymentService;
//
//    @MockitoBean
//    private PaystackWebhookService webhookService;
//
//    private PaymentResponse paymentResponse;
//    private InitiatePaymentResponse initiatePaymentResponse;
//
//    @BeforeEach
//    void setUp() {
//        // Since InitiatePaymentResponse is a record, use the constructor
//        initiatePaymentResponse = new InitiatePaymentResponse("https://paystack.com/auth", "REF-123");
//
//        // Assuming PaymentResponse is a standard class/DTO
//        paymentResponse = new PaymentResponse();
//        // Set fields for paymentResponse here...
//    }
//
//    @Test
//    void createPayment_ReturnsCreated() {
//        PaymentRequest request = new PaymentRequest();
//        request.setUserId("user123");
//        request.setMentorId("mentor456");
//        request.setAmount(5000.0);
//        request.setCurrency("NGN");
//
//        when(paymentService.createPayment(any(PaymentRequest.class))).thenReturn(initiatePaymentResponse);
//
//        webTestClient.post()
//                .uri("/api/v1/payment/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody()
//                .jsonPath("$.authorizationUrl").isEqualTo("https://paystack.com/auth")
//                .jsonPath("$.reference").isEqualTo("REF-123");
//    }
//
//    @Test
//    void verifyPayment_ReturnsOk() {
//        VerifyPaymentRequest request = new VerifyPaymentRequest();
//        when(paymentService.verifyPayment(any(VerifyPaymentRequest.class))).thenReturn(paymentResponse);
//
//        webTestClient.put()
//                .uri("/api/v1/payment/verify")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .exchange()
//                .expectStatus().isOk();
//    }
//
//    @Test
//    void findByUserId_ReturnsList() {
//        when(paymentService.findByUserId("user123")).thenReturn(List.of(paymentResponse));
//
//        webTestClient.get()
//                .uri("/api/v1/payment/user/user123")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(PaymentResponse.class)
//                .hasSize(1);
//    }
//
//    @Test
//    void handleWebhook_ValidSignature_ReturnsOk() {
//        String payload = "{\"event\":\"charge.success\"}";
//        String signature = "valid-sig";
//
//        when(webhookService.verifySignature(anyString(), eq(signature), any())).thenReturn(true);
//
//        webTestClient.post()
//                .uri("/api/v1/payment/paystack")
//                .header("x-paystack-signature", signature)
//                .bodyValue(payload)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(String.class).isEqualTo("Webhook received");
//    }
//
//    @Test
//    void handleWebhook_InvalidSignature_ReturnsUnauthorized() {
//        String payload = "{}";
//        String signature = "invalid-sig";
//
//        when(webhookService.verifySignature(anyString(), eq(signature), any())).thenReturn(false);
//
//        webTestClient.post()
//                .uri("/api/v1/payment/paystack")
//                .header("x-paystack-signature", signature)
//                .bodyValue(payload)
//                .exchange()
//                .expectStatus().isUnauthorized();
//    }
//}