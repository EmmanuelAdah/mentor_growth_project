package com.server.mentorgrowth.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Mentor extends User {
    private BigDecimal balance;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<Experience> experiences;
}
