package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.response.NotificationResponse;
import com.server.mentorgrowth.services.NotificationServiceImpl;
import com.server.mentorgrowth.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationServiceImpl notificationService;

    @GetMapping("/get")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(notificationService.findByUserId(user.getId()));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<NotificationResponse> findById(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(notificationService.findById(id, user.getId()));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable String id, @AuthenticationPrincipal User user){
        notificationService.deleteById(id, user.getId());
    }

    @DeleteMapping("/delete")
    public void deleteByUserId(@AuthenticationPrincipal User user){
        notificationService.deleteByUserId(user.getId());
    }
}
