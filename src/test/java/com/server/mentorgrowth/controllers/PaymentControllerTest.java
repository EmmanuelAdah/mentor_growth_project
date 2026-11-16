package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.requests.VerifyPaymentRequest;
import com.server.mentorgrowth.dtos.response.InitiatePaymentResponse;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.enums.Role;
import com.server.mentorgrowth.enums.User;
import com.server.mentorgrowth.services.PaystackWebhookService;
import com.server.mentorgrowth.services.interfaces.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {
    private static final String USER_ID = "mentee-123";
    private static final String PAYMENT_ID = "payment-456";
    private static final String REFERENCE = "reference-789";

    @Mock
    private PaymentService paymentService;

    @Mock
    private PaystackWebhookService webhookService;

    @InjectMocks
    private PaymentController controller;

    private User user;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        user = User.builder().id(USER_ID).role(Role.ROLE_MENTEE).build();
        paymentResponse = new PaymentResponse();
        paymentResponse.setId(PAYMENT_ID);
        paymentResponse.setUserId(USER_ID);
        paymentResponse.setReference(REFERENCE);
        ReflectionTestUtils.setField(controller, "paystackSecret", "test-secret");
    }

    @Test
    void createPaymentReturnsCreatedAndUsesAuthenticatedUserId() {
        PaymentRequest request = paymentRequest();
        InitiatePaymentResponse expected = new InitiatePaymentResponse(
                "https://paystack.example/authorize", REFERENCE);
        when(paymentService.createPayment(request, USER_ID)).thenReturn(expected);

        var response = controller.createPayment(user, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(paymentService).createPayment(request, USER_ID);
    }

    @Test
    void verifyPaymentUsesAuthenticatedUserId() {
        VerifyPaymentRequest request = verifyRequest();
        when(paymentService.verifyPayment(request, USER_ID)).thenReturn(paymentResponse);

        var response = controller.verifyPayment(user, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentResponse, response.getBody());
        verify(paymentService).verifyPayment(request, USER_ID);
    }

    @Test
    void findByIdUsesAuthenticatedUserId() {
        when(paymentService.findById(PAYMENT_ID, USER_ID)).thenReturn(paymentResponse);

        var response = controller.findById(PAYMENT_ID, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentResponse, response.getBody());
        verify(paymentService).findById(PAYMENT_ID, USER_ID);
    }

    @Test
    void findByUserIdReturnsPaymentsForAuthenticatedUser() {
        List<PaymentResponse> expected = List.of(paymentResponse);
        when(paymentService.findByUserId(USER_ID)).thenReturn(expected);

        var response = controller.findByUserId(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(paymentService).findByUserId(USER_ID);
    }

    @Test
    void findByReferenceUsesAuthenticatedUserId() {
        when(paymentService.findByReference(REFERENCE, USER_ID)).thenReturn(paymentResponse);

        var response = controller.findByReference(REFERENCE, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentResponse, response.getBody());
        verify(paymentService).findByReference(REFERENCE, USER_ID);
    }

    @Test
    void getAllPaymentsReturnsServiceResult() {
        List<PaymentResponse> expected = List.of(paymentResponse);
        when(paymentService.findAll()).thenReturn(expected);

        var response = controller.getAllPayments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(paymentService).findAll();
    }

    @Test
    void downloadReceiptReturnsPdfAndAttachmentFilename() {
        byte[] pdf = { 1, 2, 3 };
        when(paymentService.generatePaymentReceipt(PAYMENT_ID, USER_ID)).thenReturn(pdf);

        var response = controller.downloadReceipt(PAYMENT_ID, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals("attachment; filename=receipt-" + PAYMENT_ID + ".pdf",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertArrayEquals(pdf, response.getBody());
        verify(paymentService).generatePaymentReceipt(PAYMENT_ID, USER_ID);
    }

    @Test
    void handleWebhookProcessesPayloadWhenSignatureIsValid() {
        String payload = "{\"event\":\"charge.success\"}";
        String signature = "valid-signature";
        when(webhookService.verifySignature(payload, signature, "test-secret")).thenReturn(true);

        var response = controller.handleWebhook(signature, payload);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Webhook received", response.getBody());
        verify(webhookService).processEvent(payload);
    }

    @Test
    void handleWebhookRejectsInvalidSignatureWithoutProcessingPayload() {
        String payload = "{\"event\":\"charge.success\"}";
        String signature = "invalid-signature";
        when(webhookService.verifySignature(payload, signature, "test-secret")).thenReturn(false);

        var response = controller.handleWebhook(signature, payload);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid signature", response.getBody());
        verify(webhookService, never()).processEvent(payload);
    }

    @Test
    void serviceFailureIsNotConvertedToSuccessfulResponse() {
        PaymentRequest request = paymentRequest();
        when(paymentService.createPayment(request, USER_ID))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.createPayment(user, request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    private PaymentRequest paymentRequest() {
        PaymentRequest request = new PaymentRequest();
        request.setMentorId("mentor-123");
        request.setAmount(5000);
        request.setCurrency("NGN");
        return request;
    }

    private VerifyPaymentRequest verifyRequest() {
        VerifyPaymentRequest request = new VerifyPaymentRequest();
        request.setPaymentReference(REFERENCE);
        return request;
    }
}
