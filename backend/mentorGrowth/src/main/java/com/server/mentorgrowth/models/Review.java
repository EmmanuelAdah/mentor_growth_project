package com.server.mentorgrowth.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String mentorId;
    private String menteeId;

    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
