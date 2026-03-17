package com.server.mentorgrowth.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
public class PaystackWebhookService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentServiceImpl paymentService;

    public PaystackWebhookService(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    public boolean verifySignature(String payload, String signature, String secret) {
        try {
            Mac sha512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
            sha512.init(keySpec);

            byte[] hash = sha512.doFinal(payload.getBytes());
            String computedSignature = Hex.encodeHexString(hash);

            return computedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    public void processEvent(String payload) {
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            String event = jsonNode.get("event").asText();

            if ("charge.success".equals(event)) {

                JsonNode data = jsonNode.get("data");
                String reference = data.get("reference").asText();
                String status = data.get("status").asText();
                long amount = data.get("amount").asLong();

                paymentService.handleSuccessfulPayment(reference, amount, status);
            }
        } catch (Exception e) {
            throw new RuntimeException("Webhook processing failed", e);
        }
    }
}
