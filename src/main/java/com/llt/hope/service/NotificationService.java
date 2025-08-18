package com.llt.hope.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.dto.response.NotificationResponse;
import com.llt.hope.entity.Notification;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.NotificationMapper;
import com.llt.hope.repository.jpa.NotificationRepository;
import com.llt.hope.repository.jpa.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationService {
    
    UserRepository userRepository;
    NotificationRepository notificationRepository;
    SimpMessagingTemplate messagingTemplate;
    NotificationMapper notificationMapper;

    @Transactional
    public NotificationResponse createNotification(String userEmail, String title, String message, String type) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notification = notificationRepository.save(notification);
        NotificationResponse response = notificationMapper.toNotificationResponse(notification);

        // Send notification via WebSocket
        sendNotificationToUser(userEmail, response);

        return response;
    }

    /**
     * Send notification to specific user via WebSocket
     */
    public void sendNotificationToUser(String userEmail, NotificationResponse notification) {
        log.info("Sending notification to user: {} - Title: {}", userEmail, notification.getTitle());
        messagingTemplate.convertAndSendToUser(userEmail, "/queue/notifications", notification);
        
        // Also send to topic for general notification updates
        messagingTemplate.convertAndSend("/topic/notifications/" + userEmail, notification);
    }

    /**
     * Send real-time notification without persisting
     */
    public void sendRealTimeNotification(String userEmail, String title, String message, String type) {
        NotificationResponse notification = NotificationResponse.builder()
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        sendNotificationToUser(userEmail, notification);
    }

    /**
     * Broadcast notification to all users
     */
    public void broadcastNotification(String title, String message, String type) {
        NotificationResponse notification = NotificationResponse.builder()
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/notifications/broadcast", notification);
        log.info("Broadcasted notification: {}", title);
    }

    /**
     * Send notification to users in a specific role
     */
    public void sendNotificationToRole(String roleName, String title, String message, String type) {
        List<User> users = userRepository.findByRoleName(roleName);
        
        NotificationResponse notification = NotificationResponse.builder()
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        users.forEach(user -> {
            sendNotificationToUser(user.getEmail(), notification);
            // Optionally save to database
            createNotification(user.getEmail(), title, message, type);
        });
        
        log.info("Sent notification to {} users with role: {}", users.size(), roleName);
    }

    /**
     * Get all notifications for a user
     */
    public List<NotificationResponse> getUserNotifications(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        return notifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get unread notifications count
     */
    public long getUnreadNotificationsCount(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    /**
     * Mark notification as read
     */
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
        
        notification.setRead(true);
        notificationRepository.save(notification);
        
        // Send update via WebSocket
        NotificationResponse response = notificationMapper.toNotificationResponse(notification);
        sendNotificationToUser(notification.getUser().getEmail(), response);
    }

    /**
     * Mark all notifications as read for a user
     */
    @Transactional
    public void markAllAsRead(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        List<Notification> unreadNotifications = notificationRepository.findByUserAndIsReadFalse(user);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
        
        // Send update via WebSocket
        messagingTemplate.convertAndSendToUser(userEmail, "/queue/notifications/read-all", "All notifications marked as read");
    }

    /**
     * Delete notification
     */
    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
        
        String userEmail = notification.getUser().getEmail();
        notificationRepository.delete(notification);
        
        // Send delete update via WebSocket
        messagingTemplate.convertAndSendToUser(userEmail, "/queue/notifications/deleted", notificationId);
    }
}
