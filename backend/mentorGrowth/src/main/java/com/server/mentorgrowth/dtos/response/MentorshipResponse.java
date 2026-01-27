package com.server.mentorgrowth.dtos.response;

import com.server.mentorgrowth.models.Goal;
import com.server.mentorgrowth.models.MentorshipStatus;
import com.server.mentorgrowth.models.User;
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
    private User mentee;
    private User mentor;
    private List<Goal> goals;
    private MentorshipStatus status;
    private LocalDateTime createdAt;
}
