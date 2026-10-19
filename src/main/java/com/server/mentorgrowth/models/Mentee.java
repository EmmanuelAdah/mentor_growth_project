package com.server.mentorgrowth.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Mentee extends User {
    @Enumerated(EnumType.STRING)
    private CareerStage careerStage;
}
