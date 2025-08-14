package com.llt.hope.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.NotificationRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.NotificationResponse;
import com.llt.hope.service.NotificationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationController {
    
    NotificationService notificationService;

    // REST API endpoints
    @PostMapping("/api/notifications/create")
    @ResponseBody
    public ApiResponse<NotificationResponse> createNotification(@RequestBody NotificationRequest request) {
        return ApiResponse.<NotificationResponse>builder()
                .result(notificationService.createNotification(
                        request.getUserEmail(), 
                        request.getTitle(), 
                        request.getMessage(), 
                        request.getType()))
                .build();
    }

    @GetMapping("/api/notifications/{userEmail}")
    @ResponseBody
    public ApiResponse<List<NotificationResponse>> getUserNotifications(@PathVariable String userEmail) {
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getUserNotifications(userEmail))
                .build();
    }

    @GetMapping("/api/notifications/{userEmail}/unread-count")
    @ResponseBody
    public ApiResponse<Long> getUnreadNotificationsCount(@PathVariable String userEmail) {
        return ApiResponse.<Long>builder()
                .result(notificationService.getUnreadNotificationsCount(userEmail))
                .build();
    }

    @PostMapping("/api/notifications/{notificationId}/mark-read")
    @ResponseBody
    public ApiResponse<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ApiResponse.<String>builder()
                .result("Notification marked as read")
                .build();
    }

    @PostMapping("/api/notifications/{userEmail}/mark-all-read")
    @ResponseBody
    public ApiResponse<String> markAllAsRead(@PathVariable String userEmail) {
        notificationService.markAllAsRead(userEmail);
        return ApiResponse.<String>builder()
                .result("All notifications marked as read")
                .build();
    }

    @DeleteMapping("/api/notifications/{notificationId}")
    @ResponseBody
    public ApiResponse<String> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ApiResponse.<String>builder()
                .result("Notification deleted")
                .build();
    }

    @PostMapping("/api/notifications/send-to-role")
    @ResponseBody
    public ApiResponse<String> sendNotificationToRole(@RequestBody NotificationRequest request) {
        notificationService.sendNotificationToRole(
                request.getRoleName(), 
                request.getTitle(), 
                request.getMessage(), 
                request.getType()
        );
        return ApiResponse.<String>builder()
                .result("Notification sent to all users in role: " + request.getRoleName())
                .build();
    }

    @PostMapping("/api/notifications/broadcast")
    @ResponseBody
    public ApiResponse<String> broadcastNotification(@RequestBody NotificationRequest request) {
        notificationService.broadcastNotification(request.getTitle(), request.getMessage(), request.getType());
        return ApiResponse.<String>builder()
                .result("Notification broadcasted to all users")
                .build();
    }

    // WebSocket endpoints
    /**
     * Send notification to specific user via WebSocket
     */
    @MessageMapping("/notification.send")
    @SendToUser("/queue/notifications")
    public NotificationResponse sendNotification(NotificationRequest request, Principal principal) {
        log.info("Sending notification via WebSocket to: {}", request.getUserEmail());
        
        return notificationService.createNotification(
                request.getUserEmail(), 
                request.getTitle(), 
                request.getMessage(), 
                request.getType()
        );
    }

    /**
     * Send real-time notification without persisting
     */
    @MessageMapping("/notification.realtime")
    public void sendRealTimeNotification(NotificationRequest request, Principal principal) {
        log.info("Sending real-time notification to: {}", request.getUserEmail());
        
        notificationService.sendRealTimeNotification(
                request.getUserEmail(), 
                request.getTitle(), 
                request.getMessage(), 
                request.getType()
        );
    }

    /**
     * Broadcast notification to all connected users
     */
    @MessageMapping("/notification.broadcast")
    @SendTo("/topic/notifications/broadcast")
    public NotificationResponse broadcastNotificationWS(NotificationRequest request, Principal principal) {
        log.info("Broadcasting notification: {}", request.getTitle());
        
        notificationService.broadcastNotification(request.getTitle(), request.getMessage(), request.getType());
        
        return NotificationResponse.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .isRead(false)
                .createdAt(java.time.LocalDateTime.now())
                .build();
    }

    /**
     * Send notification to users with specific role
     */
    @MessageMapping("/notification.role")
    public void sendNotificationToRoleWS(NotificationRequest request, Principal principal) {
        log.info("Sending notification to role: {}", request.getRoleName());
        
        notificationService.sendNotificationToRole(
                request.getRoleName(), 
                request.getTitle(), 
                request.getMessage(), 
                request.getType()
        );
    }

    /**
     * Mark notification as read via WebSocket
     */
    @MessageMapping("/notification.read")
    public void markNotificationAsRead(@RequestBody MarkReadRequest request, Principal principal) {
        log.info("Marking notification {} as read", request.getNotificationId());
        notificationService.markAsRead(request.getNotificationId());
    }

    /**
     * Delete notification via WebSocket
     */
    @MessageMapping("/notification.delete")
    public void deleteNotificationWS(@RequestBody DeleteNotificationRequest request, Principal principal) {
        log.info("Deleting notification {}", request.getNotificationId());
        notificationService.deleteNotification(request.getNotificationId());
    }

    // Helper classes for WebSocket requests
    public static class MarkReadRequest {
        private Long notificationId;

        public Long getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(Long notificationId) {
            this.notificationId = notificationId;
        }
    }

    public static class DeleteNotificationRequest {
        private Long notificationId;

        public Long getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(Long notificationId) {
            this.notificationId = notificationId;
        }
    }
}
