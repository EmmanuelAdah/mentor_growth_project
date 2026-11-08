package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.NotificationRequest;
import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.requests.VerifyPaymentRequest;
import com.server.mentorgrowth.dtos.response.InitiatePaymentResponse;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.exceptions.*;
import com.server.mentorgrowth.models.Payment;
import com.server.mentorgrowth.models.Role;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.PaymentRepository;
import com.server.mentorgrowth.services.interfaces.PaymentService;
import com.server.mentorgrowth.utils.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.time.Duration;
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
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ModelMapper modelMapper;
    private final PdfGeneratorService pdfGeneratorService;

    @Value("${paystack.apiKey}")
    private String secretKey;

    @Value("${paystack.uri}")
    private String baseUri;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Override
    @Transactional
    public InitiatePaymentResponse createPayment(PaymentRequest request, String currentUserId) {
        log.info("Initiating payment for mentee: {} and mentor: {}", currentUserId, request.getMentorId());

        User mentee = userService.getUserById(currentUserId);
        User mentor = userService.getUserById(request.getMentorId());
        if (mentee.getRole() != Role.ROLE_MENTEE || mentor.getRole() != Role.ROLE_MENTOR) {
            throw new AccessDeniedException("Only a mentee can pay a mentor");
        }

        Map<String, Object> paymentDetails = Map.of(
                "email", mentee.getEmail(),
                "amount", Math.round(request.getAmount() * 100),
                "currency", request.getCurrency()
        );

        String paymentResponse = webClient.post()
                .uri(baseUri + "/initialize")
                .header("Authorization", "Bearer " + secretKey)
                .header("Content-type", "application/json")
                .bodyValue(paymentDetails)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Map<String, Object> response = processPaymentResponse(paymentResponse);
        Payment payment = mapPayment(mentor, mentee, request, response);
        Payment savedPayment = paymentRepository.save(payment);

        String reference = response.get("reference").toString();
        redisTemplate.opsForValue()
                .set("Payment: " + reference, savedPayment.getId(), Duration.ofMinutes(10));

        log.info("Payment initiated via reference: {}", reference);
        String authUrl = response.get("authorizationUrl").toString();
        return new InitiatePaymentResponse(authUrl, reference);
    }

    @Override
    @Transactional
    public PaymentResponse verifyPayment(VerifyPaymentRequest request, String currentUserId) {
        Payment existingPayment = paymentRepository.findByReferenceAndParticipantId(
                        request.getPaymentReference(), currentUserId)
                .orElseThrow(() -> new InvalidPaymentReferenceException(
                        "Invalid payment reference"));

        if ("success".equalsIgnoreCase(existingPayment.getStatus())) {
            return mapPayment(existingPayment);
        }

        String verificationDetails = webClient.get()
                .uri(baseUri + "/verify/" + existingPayment.getReference())
                .header("Authorization", "Bearer " + secretKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String paymentStatus = getPaymentStatus(verificationDetails, existingPayment);
        existingPayment.setStatus(paymentStatus);
        Payment payment = paymentRepository.save(existingPayment);

        if ("success".equalsIgnoreCase(paymentStatus)) {
            notifyUsers(payment);
        }
        return mapPayment(payment);
    }

    @Transactional
    public void handleSuccessfulPayment(
            String reference,
            double amountInMajorUnits,
            String currency,
            String status) {
        Payment payment = paymentRepository.findByReference(reference).orElse(null);

        if (payment == null) {
            log.error("Payment not found for reference: {}", reference);
            return;
        }

        if ("success".equalsIgnoreCase(payment.getStatus())) {
            log.info("Ignoring duplicate webhook for already successful payment: {}", reference);
            return;
        }

        if (!"success".equalsIgnoreCase(status)
                || Math.round(amountInMajorUnits * 100) != Math.round(payment.getAmount() * 100)
                || currency == null
                || !currency.equalsIgnoreCase(payment.getCurrency())) {
            throw new IncompleteTransactionException("Payment details did not match the initiated transaction");
        }

        payment.setStatus("success");
        Payment saved = paymentRepository.save(payment);

        notifyUsers(saved);
    }

    private void notifyUsers(Payment payment) {
        String message = String.format(
                "You have received a payment of %s %.2f from %s for your services.",
                payment.getCurrency(),
                payment.getAmount(),
                payment.getMentee().getFirstName() + " " + payment.getMentee().getLastName()
        );
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(payment.getMentor().getId())
                .title("Payment Received")
                .message(message)
                .build();
        rabbitTemplate.convertAndSend(exchangeName, routingKey, notificationRequest);
    }

    private String getPaymentStatus(String verificationDetails, Payment payment) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(verificationDetails).path("data");
        String reference = data.path("reference").asString();
        long amount = data.path("amount").asLong();
        String currency = data.path("currency").asString();

        if (data.isMissingNode()
                || !payment.getReference().equals(reference)
                || amount != Math.round(payment.getAmount() * 100)
                || currency == null
                || !currency.equalsIgnoreCase(payment.getCurrency())) {
            throw new IncompleteTransactionException("Payment details did not match the initiated transaction");
        }

        return data.path("status").asString();
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
    public PaymentResponse findById(String id, String currentUserId) {
        return paymentRepository.findByIdAndParticipantId(id, currentUserId)
                .map(Mapper::mapPayment)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
    }

    @Override
    public List<PaymentResponse> findAll() {
        List<Payment> payments = paymentRepository.findAll();

        if (payments.isEmpty()) {
            throw new NoPaymentFoundException("No payments found");
        }

        return payments
                .stream()
                .map(payment -> modelMapper.map(payment, PaymentResponse.class))
                .toList();
    }

    @Override
    public List<PaymentResponse> findByUserId(String userId) {
        List<Payment> payments = paymentRepository.findByParticipantId(userId);

        if (payments.isEmpty()) {
            throw new NoPaymentFoundException("No payments found with user ID: " + userId);
        }

        return payments.stream()
                .map(payment -> modelMapper.map(payment, PaymentResponse.class))
                .toList();
    }

    @Override
    public PaymentResponse findByReference(String reference, String currentUserId) {
        return paymentRepository.findByReferenceAndParticipantId(reference, currentUserId)
                .map(payment -> modelMapper.map(payment, PaymentResponse.class))
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
    }

    @Override
    public byte[] generatePaymentReceipt(String paymentId, String userId) {
        Payment payment = paymentRepository.findByIdAndParticipantId(paymentId, userId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        return pdfGeneratorService.generateHtmlPdf(payment);
    }
}
