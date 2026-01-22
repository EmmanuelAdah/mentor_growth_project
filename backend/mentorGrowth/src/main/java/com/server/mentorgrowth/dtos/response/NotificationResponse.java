package com.server.mentorgrowth.dtos.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private String id;
    private String userId;
    private String message;
    private LocalDateTime createdAt;
}
