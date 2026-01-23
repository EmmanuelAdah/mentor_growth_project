package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.response.NotificationResponse;
import com.server.mentorgrowth.services.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationServiceImpl notificationService;

    @GetMapping("/get/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.findByUserId(userId));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<NotificationResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(notificationService.findById(id));
    }

    @DeleteMapping("/delete/id")
    public void deleteById(@RequestParam String id){
        notificationService.deleteById(id);
    }

    @DeleteMapping("/delete/user/userId")
    public void deleteByUserId(@RequestParam String userId){
        notificationService.deleteByUserId(userId);
    }
}
