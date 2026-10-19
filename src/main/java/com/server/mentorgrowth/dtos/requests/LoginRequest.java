package com.server.mentorgrowth.dtos.requests;

import com.server.mentorgrowth.customAnnotations.ValidateEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @ValidateEmail
    private String email;

    @NotBlank
    private String password;
}
