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

    @Column(nullable = false)
    private double pricing = 0.0;

    private BigDecimal earnings;
}


