package com.server.mentorgrowth.utils;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.requests.RegisterRequest;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.InvalidRoleException;
import com.server.mentorgrowth.models.Payment;
import com.server.mentorgrowth.models.Role;
import com.server.mentorgrowth.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Objects;

public class Mapper {

    public static User map(RegisterRequest request){
        User user = new User();
        user.setFirstName(request.getFirstName()
                .toUpperCase().trim());
        user.setLastName(request.getLastName()
                .toUpperCase().trim());
        user.setEmail(request.getEmail());
        try {
            user.setRole(Role.valueOf(request.getRole()
                    .toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Invalid role");
        }
        user.setPassword(request.getPassword());

        return user;
    }

    public static UserResponse map(User user){
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .phoneNumber(user.getPhoneNumber())
                .profession(user.getProfession())
                .linkedin(user.getLinkedin())
                .profileImage(user.getProfileImage())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static Payment mapPayment(String mentorId,
                              String userId,
                              PaymentRequest request,
                              Map<String, Object> map){

        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setMentorId(mentorId);
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setReference(map.get("reference").toString());
        payment.setStatus("pending");

        return payment;
    }

    public static PaymentResponse mapPayment(Payment payment){
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(payment.getId());
        paymentResponse.setUserId(payment.getUserId());
        paymentResponse.setMentorId(payment.getMentorId());
        paymentResponse.setStatus(payment.getStatus());
        paymentResponse.setAmount(payment.getAmount());
        paymentResponse.setCurrency(payment.getCurrency());
        paymentResponse.setCreatedAt(payment.getCreatedAt());

        return paymentResponse;
    }
}
