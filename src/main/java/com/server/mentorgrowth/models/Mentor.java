package com.server.mentorgrowth.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Mentor extends User {
    private String rating;

//    @ElementCollection
//    @CollectionTable(
//            name = "mentor_services",
//            joinColumns = @JoinColumn(name = "mentor_id")
//    )
//    private List<Service> services;

    private double pricing = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal earnings;
}


