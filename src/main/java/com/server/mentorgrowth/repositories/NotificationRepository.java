package com.server.mentorgrowth.repositories;

import com.server.mentorgrowth.models.Notification;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Nullable List<Notification> findByUserId(String userId);

    @Nullable Optional<Notification> findById(String id);

    void deleteById(String id);

    void deleteByUserId(String userId);
}
