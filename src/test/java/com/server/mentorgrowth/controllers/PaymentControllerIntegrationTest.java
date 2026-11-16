package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.response.InitiatePaymentResponse;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.enums.Role;
import com.server.mentorgrowth.enums.User;
import com.server.mentorgrowth.services.PaystackWebhookService;
import com.server.mentorgrowth.services.interfaces.PaymentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PaymentControllerIntegrationTest {
    private static final String USER_ID = "mentee-123";
    private static final String PAYMENT_ID = "payment-456";
    private static final String REFERENCE = "reference-789";

    @Mock
    private PaymentService paymentService;

    @Mock
    private PaystackWebhookService webhookService;

    @InjectMocks
    private PaymentController controller;

    private MockMvc mockMvc;
    private LocalValidatorFactoryBean validator;
    private User mentee;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(controller, "paystackSecret", "test-secret");
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .setValidator(validator)
                .build();

        mentee = User.builder().id(USER_ID).email("mentee@example.com").role(Role.ROLE_MENTEE).build();
        authenticateAs(mentee);
        paymentResponse = new PaymentResponse();
        paymentResponse.setId(PAYMENT_ID);
        paymentResponse.setUserId(USER_ID);
        paymentResponse.setMentorId("mentor-123");
        paymentResponse.setReference(REFERENCE);
        paymentResponse.setAmount(5000);
        paymentResponse.setCurrency("NGN");
        paymentResponse.setStatus("pending");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        validator.close();
    }

    @Test
    void createPaymentMapsRequestAndReturnsCreatedResponse() throws Exception {
        when(paymentService.createPayment(any(), org.mockito.ArgumentMatchers.eq(USER_ID)))
                .thenReturn(new InitiatePaymentResponse("https://paystack.example/authorize", REFERENCE));

        mockMvc.perform(post("/api/v1/payment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"mentorId":"mentor-123","amount":5000,"currency":"NGN"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authorizationUrl")
                        .value("https://paystack.example/authorize"))
                .andExpect(jsonPath("$.reference").value(REFERENCE));

        verify(paymentService).createPayment(any(), org.mockito.ArgumentMatchers.eq(USER_ID));
    }

    @Test
    void createPaymentRejectsInvalidRequestBeforeCallingService() throws Exception {
        mockMvc.perform(post("/api/v1/payment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"mentorId":"","amount":0,"currency":"CAD"}
                                """))
                .andExpect(status().isBadRequest());

        verify(paymentService, never()).createPayment(any(), any());
    }

    @Test
    void verifyPaymentReturnsUpdatedPayment() throws Exception {
        when(paymentService.verifyPayment(any(), org.mockito.ArgumentMatchers.eq(USER_ID)))
                .thenReturn(paymentResponse);

        mockMvc.perform(put("/api/v1/payment/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"paymentReference\":\"" + REFERENCE + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference").value(REFERENCE))
                .andExpect(jsonPath("$.status").value("pending"));

        verify(paymentService).verifyPayment(any(), org.mockito.ArgumentMatchers.eq(USER_ID));
    }

    @Test
    void paymentReadRoutesUseCurrentMappingsAndAuthenticatedUser() throws Exception {
        when(paymentService.findById(PAYMENT_ID, USER_ID)).thenReturn(paymentResponse);
        when(paymentService.findByReference(REFERENCE, USER_ID)).thenReturn(paymentResponse);
        when(paymentService.findByUserId(USER_ID)).thenReturn(List.of(paymentResponse));

        mockMvc.perform(get("/api/v1/payment/id").param("id", PAYMENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference").value(REFERENCE));

        mockMvc.perform(get("/api/v1/payment/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reference").value(REFERENCE));

        mockMvc.perform(get("/api/v1/payment/get/payment/{reference}", REFERENCE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference").value(REFERENCE));

        verify(paymentService).findById(PAYMENT_ID, USER_ID);
        verify(paymentService).findByUserId(USER_ID);
        verify(paymentService).findByReference(REFERENCE, USER_ID);
    }

    @Test
    void adminPaymentRouteReturnsAllPayments() throws Exception {
        User admin = User.builder().id("admin-123").role(Role.ROLE_ADMIN).build();
        authenticateAs(admin);
        when(paymentService.findAll()).thenReturn(List.of(paymentResponse));

        mockMvc.perform(get("/api/v1/payment/get/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].reference").value(REFERENCE));

        verify(paymentService).findAll();
    }

    @Test
    void downloadReceiptReturnsPdfAttachment() throws Exception {
        byte[] receipt = { 1, 2, 3 };
        when(paymentService.generatePaymentReceipt(PAYMENT_ID, USER_ID)).thenReturn(receipt);

        mockMvc.perform(get("/api/v1/payment/{paymentId}/receipt", PAYMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=receipt-" + PAYMENT_ID + ".pdf"))
                .andExpect(content().bytes(receipt));

        verify(paymentService).generatePaymentReceipt(PAYMENT_ID, USER_ID);
    }

    @Test
    void webhookProcessesValidSignatureAndRejectsInvalidSignature() throws Exception {
        String payload = "{\"event\":\"charge.success\"}";
        when(webhookService.verifySignature(payload, "valid-signature", "test-secret")).thenReturn(true);
        when(webhookService.verifySignature(payload, "invalid-signature", "test-secret")).thenReturn(false);

        mockMvc.perform(post("/api/v1/payment/paystack")
                        .header("x-paystack-signature", "valid-signature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().string("Webhook received"));

        mockMvc.perform(post("/api/v1/payment/paystack")
                        .header("x-paystack-signature", "invalid-signature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid signature"));

        verify(webhookService).processEvent(payload);
    }

    private void authenticateAs(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
