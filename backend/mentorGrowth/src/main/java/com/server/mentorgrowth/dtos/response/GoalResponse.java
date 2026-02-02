package com.server.mentorgrowth.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalResponse {
    private String id;
    private String title;
    private String description;
    private String mentorshipId;
    private LocalDateTime targetDate;
    private String status;
    private String createdAt;
    private String updatedAt;
}
