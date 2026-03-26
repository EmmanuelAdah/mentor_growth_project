package com.server.mentorgrowth.utils;

import com.server.mentorgrowth.dtos.requests.PaymentRequest;
import com.server.mentorgrowth.dtos.requests.RegisterRequest;
import com.server.mentorgrowth.dtos.response.PaymentResponse;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.InvalidEmailFormatException;
import com.server.mentorgrowth.exceptions.InvalidNameFormatException;
import com.server.mentorgrowth.exceptions.InvalidPasswordFormatException;
import com.server.mentorgrowth.exceptions.InvalidRoleException;
import com.server.mentorgrowth.models.*;
import org.passay.*;

import java.util.Arrays;
import java.util.Map;

public class Mapper {

    public static User map(RegisterRequest request){

        User user = new User();
        user.setFirstName(request.getFirstName()
                .toUpperCase().trim());
        user.setLastName(request.getLastName()
                .toUpperCase().trim());
        user.setEmail(request.getEmail()
                .trim()
                .toLowerCase());
        try {
            user.setRole(Role.valueOf("ROLE_" + request.getRole()
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
                .phoneNumber(user.getPhoneNumber())
                .profession(user.getProfession())
                .linkedin(user.getLinkedin())
                .profileImageUrl(user.getProfileImageUrl())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static Payment mapPayment(User mentor,
                              User mentee,
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

    public static void validateFields(RegisterRequest request){
        validateName(request.getFirstName());
        validateName(request.getLastName());
        validateEmail(request.getEmail());
        validatePassword(request.getPassword());
    }

    public static void validatePassword(String password) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 64),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()
        ));

        RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            throw new InvalidPasswordFormatException("Invalid Password: " + validator.getMessages(result));
        }
    }

    public static void validateName(String name){
        if (!name.matches("^[\\p{L}.'\\-]+$")){
            throw new InvalidNameFormatException("Invalid name format");
        }
    }

    public static void validateEmail(String email){
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z.-]+\\.[a-zA-Z]{2,6}$")){
            throw new InvalidEmailFormatException("Invalid email format");
        }
    }
}
