package com.server.mentorgrowth.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import javax.crypto.spec.SecretKeySpec;

@Service
public class PaystackWebhookService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentServiceImpl paymentService;
    private final Logger log = LoggerFactory.getLogger(PaystackWebhookService.class);

    public PaystackWebhookService(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    public boolean verifySignature(String payload, String signature, String secret) {
        if (payload == null || signature == null || secret == null || secret.isBlank()) {
            return false;
        }

        try {
            Mac sha512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            sha512.init(keySpec);

            byte[] hash = sha512.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return MessageDigest.isEqual(hash, Hex.decodeHex(signature));
        } catch (DecoderException e) {
            return false;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Unable to verify Paystack webhook signature", e);
        }
    }

    public void processEvent(String payload) {
        final JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid Paystack webhook payload", e);
        }

        if (!"charge.success".equals(jsonNode.path("event").asText())) {
            return;
        }

        JsonNode data = jsonNode.path("data");
        String reference = data.path("reference").asText();
        String status = data.path("status").asText();
        String currency = data.path("currency").asText();
        long amountMinor = data.path("amount").asLong();

        if (data.isMissingNode() || reference.isBlank() || amountMinor <= 0) {
            throw new IllegalArgumentException("Paystack webhook is missing transaction details");
        }

        log.info("Processing successful Paystack webhook");
        paymentService.handleSuccessfulPayment(reference, amountMinor / 100.0, currency, status);
    }
}
