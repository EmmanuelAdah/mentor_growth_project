package com.server.mentorgrowth.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SetGoalsRequest {
    @NotBlank
    private String mentorshipId;

    @NotEmpty
    @Valid
    private List<GoalRequest> goals;
}
