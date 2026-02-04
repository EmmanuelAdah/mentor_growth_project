package com.server.mentorgrowth.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn(name = "mentee_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User mentee;

    @JoinColumn(name = "mentor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User mentor;

    private double amount;
    private String currency;
    private String status;
    private String reference;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
