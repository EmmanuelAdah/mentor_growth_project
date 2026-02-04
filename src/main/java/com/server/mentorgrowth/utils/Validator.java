package com.server.mentorgrowth.utils;

import com.server.mentorgrowth.dtos.requests.RegisterRequest;
import com.server.mentorgrowth.exceptions.InvalidEmailFormatException;

public class Validator {

    public static void validateUser(RegisterRequest request) {
        isValidEmail(request.getEmail());
    }

    public static void isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@([a-zA-Z0-9-]{4,10})\\.[a-zA-Z]{2,3}$";
        if (!email.matches(regex)){
            throw new InvalidEmailFormatException("Invalid email format");
        }
    }
}
