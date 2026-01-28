package com.server.mentorgrowth.utils;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.requests.RegisterRequest;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.InvalidRoleException;
import com.server.mentorgrowth.models.*;
import java.util.Map;

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

    public static Payment mapPayment(Mentor mentor,
                              Mentee mentee,
                              PaymentRequest request,
                              Map<String, Object> map){

        Payment payment = new Payment();
        payment.setMentee(mentee);
        payment.setMentor(mentor);
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setReference(map.get("reference").toString());
        payment.setStatus("pending");

        return payment;
    }

    public static PaymentResponse mapPayment(Payment payment){
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setUserId(payment.getMentee().getId());
        response.setMentorId(payment.getMentor().getId());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setReference(payment.getReference());
        response.setStatus(payment.getStatus());
        response.setCreatedAt(payment.getCreatedAt());

        return response;
    }
}
