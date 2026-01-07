package com.server.mentorgrowth.models;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String menteeId;
    private String mentorId;
    private BigDecimal amount;
    private String currency;
    private String status;

    @CreationTimestamp
    private LocalDate createdAt;
}
