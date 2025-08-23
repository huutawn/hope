package com.llt.hope.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.llt.hope.dto.request.GetMessageRequest;
import com.llt.hope.dto.request.SendMessageRequest;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.MessageResponse;
import com.llt.hope.service.MessageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageController {
    MessageService messageService;

    // REST API endpoints
    @PostMapping("/messages/send")
    @ResponseBody
    public ApiResponse<MessageResponse> sendMessage(@RequestBody SendMessageRequest request) {
        return ApiResponse.<MessageResponse>builder()
                .result(messageService.sendMessage(
                        request.getSenderEmail(), request.getReceiverEmail(), request.getContent()))
                .build();
    }

    @GetMapping("/messages")
    @ResponseBody
    public ApiResponse<List<MessageResponse>> getMessages(
            @RequestParam(value = "email1") String user1Email,
            @RequestParam(value = "email2") String user2Email) {

        return ApiResponse.<List<MessageResponse>>builder()
                .result(messageService.getMessagesBetweenUsers(user1Email, user2Email))
                .build();
    }

    @PostMapping("/mark-read")
    @ResponseBody
    public ApiResponse<String> markMessagesAsRead(@RequestParam String senderEmail, @RequestParam String receiverEmail) {
        messageService.markMessagesAsRead(senderEmail, receiverEmail);
        return ApiResponse.<String>builder()
                .result("Messages marked as read")
                .build();
    }

    // WebSocket endpoints
    /**
     * Handle direct message sending via WebSocket
     */
    @MessageMapping("/message.send")
    @SendToUser("/queue/messages")
    public MessageResponse handleMessage(SendMessageRequest request, Principal principal) {
        log.info("Received WebSocket message from: {} to: {}", request.getSenderEmail(), request.getReceiverEmail());
        
        // Verify the sender is authenticated user
        if (principal != null && !principal.getName().equals(request.getSenderEmail())) {
            throw new SecurityException("Sender email does not match authenticated user");
        }
        
        return messageService.sendMessage(request.getSenderEmail(), request.getReceiverEmail(), request.getContent());
    }

    /**
     * Handle private message sending
     */
    @MessageMapping("/message.private")
    public void handlePrivateMessage(SendMessageRequest request, Principal principal) {
        log.info("Handling private message from: {} to: {}", request.getSenderEmail(), request.getReceiverEmail());
        
        if (principal != null && !principal.getName().equals(request.getSenderEmail())) {
            throw new SecurityException("Sender email does not match authenticated user");
        }
        
        messageService.sendWebSocketMessage(request.getSenderEmail(), request.getReceiverEmail(), request.getContent());
    }

    /**
     * Handle room/group message broadcasting
     */
    @MessageMapping("/message.room.{roomId}")
    @SendTo("/topic/room/{roomId}")
    public MessageResponse handleRoomMessage(@DestinationVariable String roomId, SendMessageRequest request, Principal principal) {
        log.info("Broadcasting message to room: {} from: {}", roomId, request.getSenderEmail());
        
        if (principal != null && !principal.getName().equals(request.getSenderEmail())) {
            throw new SecurityException("Sender email does not match authenticated user");
        }
        
        messageService.broadcastMessage(roomId, request.getSenderEmail(), request.getContent());
        
        return MessageResponse.builder()
                .content(request.getContent())
                .sentAt(java.time.LocalDateTime.now())
                .isRead(false)
                .build();
    }

    /**
     * Handle user typing indicator
     */
    @MessageMapping("/message.typing")
    public void handleTypingIndicator(@RequestParam String senderEmail, @RequestParam String receiverEmail, Principal principal) {
        log.info("Typing indicator from: {} to: {}", senderEmail, receiverEmail);
        
        if (principal != null && !principal.getName().equals(senderEmail)) {
            throw new SecurityException("Sender email does not match authenticated user");
        }
        
        // Send typing indicator to receiver
        messageService.sendWebSocketMessage(senderEmail, receiverEmail, "__TYPING_INDICATOR__");
    }
}
