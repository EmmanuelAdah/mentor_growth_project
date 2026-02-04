package com.server.mentorgrowth.dtos.requests;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GoalRequest {

    private String title;
    private String description;
    private LocalDate targetDate;
}
