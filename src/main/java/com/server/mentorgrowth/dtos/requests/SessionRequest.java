package com.server.mentorgrowth.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SessionRequest {
    @NotBlank
    private String mentorshipId;

    @NotBlank
    private String summary;

    @NotBlank
    private String startTime;

    @Min(1)
    private int durationInMinutes;
}
