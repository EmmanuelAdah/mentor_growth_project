package com.server.mentorgrowth.dtos.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String phoneNumber;
    private String profession;
    private String linkedin;
    private String profileImage;
    private int age;

    private LocalDateTime createdAt;
}
