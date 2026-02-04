package com.server.mentorgrowth.dtos.requests;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String email;

    @NonNull
    private String role;

    @NonNull
    private String password;
}
