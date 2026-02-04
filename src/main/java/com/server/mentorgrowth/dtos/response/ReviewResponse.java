package com.server.mentorgrowth.dtos.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private String id;
    private String userId;
    private String mentorId;
    private String comment;
    private LocalDateTime createdAt;
}
