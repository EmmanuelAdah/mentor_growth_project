package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.dtos.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final WebClient webClient;
    private final UserServiceImpl userService;

    @Value("${Paystack.apiKey}")
    private String secretKey;

    @Value("${Paystack.uri}")
    private String uri;

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        UserResponse mentee = userService.findById(request.getMenteeId());
        UserResponse mentor = userService.findById(request.getMentorId());

        log.info("Mentors details: [{}]", mentor);

        Map<String, Object> paymentDetails = Map.of(
                "email", mentee.getEmail(),
                "amount", (request.getAmount() * 100)
        );

        String paymentResponse = webClient.post()
                .uri(uri)
                .header("Authorization", "Bearer " + secretKey)
                .header("Content-type", "application/json")
                .bodyValue(paymentDetails)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("Payment Response: {}", paymentResponse);

        return new PaymentResponse();
    }

    public UserResponse processPaymentResponse(String payment) {
        return new UserResponse();
    }
}
