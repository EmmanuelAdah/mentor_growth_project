package com.server.mentorgrowth.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Mentor extends User {
    private String rating;
    private double pricing;

    @Column(nullable = false)
    private boolean isApproved = false;

    private BigDecimal earnings;
}


