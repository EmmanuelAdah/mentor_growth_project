package com.server.mentorgrowth.services;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PaystackWebhookServiceTest {
    private final PaystackWebhookService webhookService =
            new PaystackWebhookService(mock(PaymentServiceImpl.class));

    @Test
    void verifiesPaystackHmacSha512Signature() throws Exception {
        String payload = "{\"event\":\"charge.success\"}";
        String secret = "test-paystack-secret";
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
        String signature = HexFormat.of().formatHex(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));

        assertTrue(webhookService.verifySignature(payload, signature, secret));
        assertFalse(webhookService.verifySignature(payload, signature + "00", secret));
    }

    @Test
    void rejectsMalformedWebhookSignatures() {
        assertFalse(webhookService.verifySignature("payload", "not-hex", "secret"));
        assertFalse(webhookService.verifySignature("payload", null, "secret"));
    }
}
