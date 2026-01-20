package com.server.mentorgrowth.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GoalCreateRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Target date is required")
    @Future(message = "Target date must be in future")
    private LocalDate targetDate;

    @Min(1)
    @Max(5)
    private Integer priority = 1;
}
