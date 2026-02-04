package com.server.mentorgrowth.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PeerGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String description;
    private String category;

    @ManyToMany
    @JoinTable(
            name = "peer_group_members",
            joinColumns = @JoinColumn(name = "peer_group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;

    @ManyToOne
//    @JoinColumn(name = "creator_id")
    private User creator;

    private LocalDateTime createdAt;
}
