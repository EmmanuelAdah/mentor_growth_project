package com.server.mentorgrowth.models;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Mentee extends User {
    private CareerStage careerStage;
}
