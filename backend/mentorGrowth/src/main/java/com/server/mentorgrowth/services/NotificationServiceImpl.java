package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.NotificationRequest;
import com.server.mentorgrowth.dtos.response.NotificationResponse;
import com.server.mentorgrowth.exceptions.DeleteNotificationException;
import com.server.mentorgrowth.exceptions.NotificationNotFoundException;
import com.server.mentorgrowth.exceptions.UserNotFoundException;
import com.server.mentorgrowth.models.Notification;
import com.server.mentorgrowth.repositories.NotificationRepository;
import com.server.mentorgrowth.services.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final ModelMapper modelMapper;
    private final UserServiceImpl userServiceImpl;
    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void notifyUser(NotificationRequest request) {
        log.info("Received notification request: {}", request);
        Boolean isExistingUser = userServiceImpl.existById(request.getUserId());
        if (!isExistingUser) {
            throw new UserNotFoundException("User not found with id: " + request.getUserId());
        }
        Notification notification = Notification.builder()
                                                .userId(request.getUserId())
                                                .message(request.getMessage())
                                                .build();
        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification for user: {}", savedNotification.getUserId());
    }

    @Override
    public @Nullable List<NotificationResponse> findByUserId(String userId) {
        log.info("Finding notifications by user id {}", userId);
        return Objects.requireNonNull(notificationRepository.findByUserId(userId))
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationResponse.class))
                .toList();
    }

    @Override
    public @Nullable NotificationResponse findById(String id) {
        return Objects.requireNonNull(notificationRepository.findById(id))
                .map(notification -> modelMapper.map(notification, NotificationResponse.class))
                .orElseThrow(()-> new NotificationNotFoundException("Notification not found with ID: " + id));
    }

    @Override
    public void deleteById(String id) {
        try {
            notificationRepository.deleteById(id);
        } catch (Exception e) {
            throw new DeleteNotificationException("Notification not deleted successfully");
        }
    }

    @Override
    public void deleteByUserId(String userId) {
        try {
            notificationRepository.deleteByUserId(userId);
            log.info("Deleting notifications by user id {}", userId);
        } catch (Exception e) {
            throw new DeleteNotificationException("Notification not deleted successfully");
        }
    }
}
