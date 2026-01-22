package com.server.mentorgrowth.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import java.util.PriorityQueue;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mentorship_relation")
public class Mentorship {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(length = 36)
    private String id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee_id", nullable = false)
    private User mentee;

    @Enumerated(EnumType.STRING)
    private MentorshipStatus status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

//    @OneToMany(mappedBy = "mentorship_id")
//    private PriorityQueue<Goal> goals = new PriorityQueue<>();

    @CreationTimestamp
    private LocalDateTime createdDate;
}
