package com.server.mentorgrowth.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mentorshipsRelations")
public class MentorshipRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private User mentor;

    @ManyToOne
    @JoinColumn(name = "mentee_id")
    private User mentee;

    @Enumerated(EnumType.STRING)
    private MentorshipStatus status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

//    @OneToMany(mappedBy = "mentorship")
//    private List<Goal> goals;
}
