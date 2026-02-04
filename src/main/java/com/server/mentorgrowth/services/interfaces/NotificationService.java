package com.server.mentorgrowth.services.interfaces;

import com.server.mentorgrowth.dtos.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> findByUserId(String userId);
    NotificationResponse findById(String id);
    void deleteById(String id);
    void deleteByUserId(String id);
}
