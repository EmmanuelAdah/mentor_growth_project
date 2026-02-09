package com.server.mentorgrowth.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Mentor extends User {
    private String rating;

    @ElementCollection
    @CollectionTable(
            name = "mentor_services",
            joinColumns = @JoinColumn(name = "mentor_id")
    )
    private List<Service> services;

    @Column(precision = 10, scale = 2)
    private BigDecimal earnings;
}


