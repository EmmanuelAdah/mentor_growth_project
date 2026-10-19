package com.server.mentorgrowth.dtos.requests;

import com.server.mentorgrowth.customAnnotations.ValidateEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @ValidateEmail
    private String email;

    @NotBlank
    private String role;

    @NotBlank
    private String password;
}
