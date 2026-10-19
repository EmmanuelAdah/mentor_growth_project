package com.server.mentorgrowth.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MentorshipRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String mentorId;
}
