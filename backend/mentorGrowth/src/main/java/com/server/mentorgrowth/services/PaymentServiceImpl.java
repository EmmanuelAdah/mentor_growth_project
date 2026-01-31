package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.NotificationRequest;
import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.requests.VerifyPaymentRequest;
import com.server.mentorgrowth.dtos.response.InitiatePaymentResponse;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.InvalidPaymentIdentityException;
import com.server.mentorgrowth.exceptions.InvalidPaymentReferenceException;
import com.server.mentorgrowth.exceptions.NoPaymentFoundException;
import com.server.mentorgrowth.models.*;
import com.server.mentorgrowth.repositories.PaymentRepository;
import com.server.mentorgrowth.services.interfaces.PaymentService;
import com.server.mentorgrowth.utils.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static com.server.mentorgrowth.utils.Mapper.mapPayment;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final WebClient webClient;
    private final UserServiceImpl userService;
    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ModelMapper modelMapper;

    @Value("${Paystack.apiKey}")
    private String secretKey;

    @Value("${Paystack.uri}")
    private String baseUri;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Override
    public InitiatePaymentResponse createPayment(PaymentRequest request) {
        UserResponse savedMentee = userService.findById(request.getUserId());
        UserResponse savedMentor = userService.findById(request.getMentorId());

        Map<String, Object> paymentDetails = Map.of(
                "email", savedMentee.getEmail(),
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
        User mentee = modelMapper.map(Objects.requireNonNull(savedMentee), User.class);
        User mentor = modelMapper.map(Objects.requireNonNull(savedMentor), User.class);

        Payment payment = mapPayment((Mentor) mentor, (Mentee) mentee, request, response);
        log.info("Payment initiated via reference: {}", payment.getReference());

        paymentRepository.save(payment);
        String authUrl = response.get("authorizationUrl").toString();
        String reference = response.get("reference").toString();

        return new InitiatePaymentResponse(authUrl, reference);
    }

    @Override
    public PaymentResponse verifyPayment(VerifyPaymentRequest request) {

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
            notifyUsers(existingPayment, payment.getMentor());
        }
        return mapPayment(payment);
    }

    private void notifyUsers(Payment payment, User user) {
        String message = String.format(
                "You have received a payment of %s %.2f from %s for your services.",
                payment.getCurrency(),
                payment.getAmount(),
                user.getFirstName() + " " + user.getLastName()
        );
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(payment.getMentor().getId())
                .title("Payment Received")
                .message(message)
                .build();
        rabbitTemplate.convertAndSend(exchangeName, routingKey, notificationRequest);
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
                .map(payment -> modelMapper.map(payment, PaymentResponse.class))
                .toList();
    }

    @Override
    public List<PaymentResponse> findByUserId(String userId) {
        List<Payment> payments = paymentRepository.findByMentee_Id(userId);

        if (payments.isEmpty())
            throw new NoPaymentFoundException("No payments found with user ID: " + userId);

        return payments.stream()
                .map(payment -> modelMapper.map(payment, PaymentResponse.class))
                .toList();
    }

    @Override
    public PaymentResponse findByReference(String reference) {
        return paymentRepository.findByReference(reference)
                .map(payment -> modelMapper.map(payment, PaymentResponse.class))
                .orElseThrow(()-> new InvalidPaymentReferenceException("Invalid Payment Reference: " + reference));
    }
}
