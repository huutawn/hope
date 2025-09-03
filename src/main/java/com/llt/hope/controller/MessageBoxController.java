package com.llt.hope.controller;

import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.MessageBoxResponse;
import com.llt.hope.service.MessageBoxService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message-boxes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageBoxController {
    
    MessageBoxService messageBoxService;

    /**
     * Get all message boxes for the authenticated user
     */
    @GetMapping
    public ApiResponse<List<MessageBoxResponse>> getAllMessageBoxes(Authentication authentication) {
        String userEmail = authentication.getName();
        List<MessageBoxResponse> messageBoxes = messageBoxService.getAllMessageBox(userEmail);
        
        return ApiResponse.<List<MessageBoxResponse>>builder()
                .result(messageBoxes)
                .build();
    }

    /**
     * Get or create a message box with a specific user
     */
    @GetMapping("/with/{receiverEmail}")
    public ApiResponse<MessageBoxResponse> getOrCreateMessageBox(
            @PathVariable String receiverEmail,
            Authentication authentication) {
        
        String currentUserEmail = authentication.getName();
        MessageBoxResponse messageBox = messageBoxService.getOrCreateMessageBox(currentUserEmail, receiverEmail);
        
        return ApiResponse.<MessageBoxResponse>builder()
                .result(messageBox)
                .build();
    }

    /**
     * Mark messages as read in a specific message box
     */
    @PutMapping("/mark-read/{senderEmail}")
    public ApiResponse<String> markMessagesAsRead(
            @PathVariable String senderEmail,
            Authentication authentication) {
        
        String currentUserEmail = authentication.getName();
        messageBoxService.markMessagesAsRead(currentUserEmail, senderEmail);
        
        return ApiResponse.<String>builder()
                .result("Messages marked as read")
                .build();
    }

    /**
     * Get total unread message count for the authenticated user
     */
    @GetMapping("/unread-count")
    public ApiResponse<Long> getTotalUnreadCount(Authentication authentication) {
        String userEmail = authentication.getName();
        long unreadCount = messageBoxService.getTotalUnreadCount(userEmail);
        
        return ApiResponse.<Long>builder()
                .result(unreadCount)
                .build();
    }

    /**
     * Get count of message boxes with unread messages
     */
    @GetMapping("/unread-boxes-count")
    public ApiResponse<Long> getUnreadMessageBoxCount(Authentication authentication) {
        String userEmail = authentication.getName();
        long unreadBoxCount = messageBoxService.getUnreadMessageBoxCount(userEmail);
        
        return ApiResponse.<Long>builder()
                .result(unreadBoxCount)
                .build();
    }

    /**
     * Trigger manual refresh of message box notifications (for debugging)
     */
    @PostMapping("/notify-update")
    public ApiResponse<String> triggerMessageBoxUpdate(Authentication authentication) {
        String userEmail = authentication.getName();
        messageBoxService.notifyMessageBoxUpdate(userEmail);
        
        return ApiResponse.<String>builder()
                .result("Message box update notification sent")
                .build();
    }
}
