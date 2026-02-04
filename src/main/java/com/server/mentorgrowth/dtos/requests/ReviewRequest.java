package com.server.mentorgrowth.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String mentorId;

    @NotNull
    private String comment;
}
