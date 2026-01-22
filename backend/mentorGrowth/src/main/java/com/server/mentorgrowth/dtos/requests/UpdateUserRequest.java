package com.server.mentorgrowth.dtos.requests;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String bio;
    private String phoneNumber;
    private String profession;
    private String linkedin;
    private String profileImage;
    private String password;
}
