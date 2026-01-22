package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.requests.VerifyPaymentRequest;
import com.server.mentorgrowth.dtos.response.InitiatePaymentResponse;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.InvalidPaymentIdentityException;
import com.server.mentorgrowth.exceptions.InvalidPaymentReferenceException;
import com.server.mentorgrowth.exceptions.NoPaymentFoundException;
import com.server.mentorgrowth.models.Payment;
import com.server.mentorgrowth.repositories.PaymentRepository;
import com.server.mentorgrowth.utils.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import static com.server.mentorgrowth.utils.Mapper.mapPayment;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final WebClient webClient;
    private final UserServiceImpl userService;
    private final PaymentRepository paymentRepository;
    private final UserServiceImpl userServiceImpl;
    private final NotificationServiceImpl notificationService;

    @Value("${Paystack.apiKey}")
    private String secretKey;

    @Value("${Paystack.uri}")
    private String baseUri;

    @Override
    public InitiatePaymentResponse createPayment(PaymentRequest request) {

        UserResponse mentee = userService.findById(request.getUserId());
        UserResponse mentor = userService.findById(request.getMentorId());

        Map<String, Object> paymentDetails = Map.of(
                "email", mentee.getEmail(),
                "amount", (request.getAmount() * 100)
        );

        String paymentResponse = webClient.post()
                .uri(baseUri + "/initialize" )
                .header("Authorization", "Bearer " + secretKey)
                .header("Content-type", "application/json")
                .bodyValue(paymentDetails)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Map<String, Object> response = processPaymentResponse(paymentResponse);
        Payment payment = mapPayment(mentor.getId(), mentee.getId(), request, response);
        log.info("Payment initiated via reference: {}", payment.getReference());

        paymentRepository.save(payment);
        InitiatePaymentResponse initiatePaymentResponse = new InitiatePaymentResponse();
        initiatePaymentResponse.setAuthorizationUrl(response.get("authorizationUrl").toString());
        initiatePaymentResponse.setReference(response.get("reference").toString());

        return initiatePaymentResponse;
    }

    @Override
    public PaymentResponse verifyPayment(VerifyPaymentRequest request) {
        UserResponse existingUser = userServiceImpl.findById(request.getUserId());
        log.info("Verify Payment Request: {}", request.getPaymentReference());

        Payment existingPayment = paymentRepository.findByReference(request.getPaymentReference())
                .orElseThrow(() -> new InvalidPaymentReferenceException("Invalid payment reference: " + request.getPaymentReference()));

        String verificationDetails = webClient.get()
                                                .uri(baseUri + "/verify/" + existingPayment.getReference())
                                                .header("Authorization", "Bearer " + secretKey)
                                                .retrieve()
                                                .bodyToMono(String.class)
                                                .block();

        String paymentStatus = getPaymentStatus(verificationDetails);
        existingPayment.setStatus(paymentStatus);
        Payment payment = paymentRepository.save(existingPayment);
        if(paymentStatus.equals("success")) {
            String message = prepareNoticiation(
                    existingUser.getFirstName(),
                    existingUser.getLastName(),
                    existingPayment.getAmount(),
                    existingPayment.getCurrency()
            );
            notificationService.notifyUser(existingPayment.getMentorId(), message);
        }
        return Mapper.mapPayment(payment);
    }

    private String prepareNoticiation(String firstName, String lastName, double amount, String currency) {
        return String.format("New payment of %s %f.2 recieved from %s for your services", currency, amount, firstName + " " + lastName);
    }

    private String getPaymentStatus(String verificationDetails) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode payload = mapper.readTree(verificationDetails);

        return payload.get("data")
                        .path("status")
                        .asString();
    }

    public Map<String, Object> processPaymentResponse(String payment) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode paymentNode = mapper.readTree(payment);

        String authUrl = paymentNode
                .get("data")
                .path("authorization_url")
                .asString();

        String reference = paymentNode
                .get("data")
                .path("reference")
                .asString();

        return Map.of("authorizationUrl", authUrl, "reference", reference);
    }

    @Override
    public PaymentResponse findById(String id) {
        return paymentRepository.findById(id)
                .map(Mapper::mapPayment)
                .orElseThrow(() -> new InvalidPaymentIdentityException("Invalid payment ID: " + id));
    }

    @Override
    public List<PaymentResponse> findAll() {
        List<Payment> payments = paymentRepository.findAll();

        if (payments.isEmpty())
            throw new NoPaymentFoundException("No payments found");

        return payments
                .stream()
                .map(Mapper::mapPayment)
                .toList();
    }

    @Override
    public List<PaymentResponse> findByUserId(String userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);

        if (payments.isEmpty())
            throw new NoPaymentFoundException("No payments found with user ID: " + userId);

        return payments.stream()
                .map(Mapper::mapPayment)
                .toList();
    }

    @Override
    public PaymentResponse findByReference(String reference) {
        return paymentRepository.findByReference(reference)
                .map(Mapper::mapPayment)
                .orElseThrow(()-> new InvalidPaymentReferenceException("Invalid Payment Reference: " + reference));
    }
}
