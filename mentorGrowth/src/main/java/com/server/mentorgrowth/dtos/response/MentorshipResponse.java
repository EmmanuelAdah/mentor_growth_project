package com.server.mentorgrowth.dtos.response;

import com.server.mentorgrowth.models.MentorshipStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipResponse {
    private String id;
    private UserResponse mentee;
    private UserResponse mentor;
    private List<GoalResponse> goals;
    private MentorshipStatus status;
    private LocalDateTime createdAt;
}
