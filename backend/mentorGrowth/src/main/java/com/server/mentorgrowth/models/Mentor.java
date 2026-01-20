package com.server.mentorgrowth.models;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Mentor extends User {
    private String rating;
    private double pricing;
    private BigDecimal earnings;
}
