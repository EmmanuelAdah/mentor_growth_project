package com.server.mentorgrowth.utils;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.requests.UserRequest;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.InvalidRoleException;
import com.server.mentorgrowth.models.Payment;
import com.server.mentorgrowth.models.Role;
import com.server.mentorgrowth.models.User;

import java.util.Map;

public class Mapper {

    public static User map(UserRequest request){
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
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole().name());
        userResponse.setCreatedAt(user.getCreatedAt());

        return userResponse;
    }

    public static Payment mapPayment(String mentorId,
                              String menteeId,
                              PaymentRequest request,
                              Map<String, Object> map){

        Payment payment = new Payment();
        payment.setMenteeId(menteeId);
        payment.setMentorId(mentorId);
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setReference(map.get("reference").toString());
        payment.setStatus("pending");

        return payment;
    }

    public static PaymentResponse mapPaymentResponse(Payment payment){
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(payment.getId());
        paymentResponse.setUserId(payment.getMenteeId());
        paymentResponse.setMentorId(payment.getMentorId());
        paymentResponse.setStatus(payment.getStatus());
        paymentResponse.setAmount(payment.getAmount());
        paymentResponse.setCurrency(payment.getCurrency());

        return paymentResponse;
    }
}
