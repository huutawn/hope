package com.llt.hope.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.llt.hope.dto.response.MessageResponse;
import com.llt.hope.entity.Message;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.MessageMapper;
import com.llt.hope.repository.jpa.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageService {
    UserRepository userRepository;
    MessageRepository messageRepository;
    SimpMessagingTemplate messagingTemplate;
    MessageMapper messageMapper;

    @Transactional
    public MessageResponse sendMessage(String senderEmail, String receiverEmail, String content) {
        // Kiểm tra người dùng
        User sender =
                userRepository.findByEmail(senderEmail).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User receiver = userRepository
                .findByEmail(receiverEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tạo tin nhắn mới
        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .build();

        message = messageRepository.save(message);
        MessageResponse messageResponse = messageMapper.toMessageResponse(message);

        // Gửi tin nhắn qua WebSocket đến người nhận
        log.info("Sending message to user: {} on topic: /queue/messages", receiverEmail);
        messagingTemplate.convertAndSendToUser(receiverEmail, "/queue/messages", messageResponse);
        
        // Gửi thông báo đến topic chung để cập nhật danh sách conversation
        messagingTemplate.convertAndSend("/topic/messages/" + receiverEmail, messageResponse);

        return messageResponse;
    }
    
    /**
     * Send message via WebSocket directly (for real-time messaging)
     */
    public void sendWebSocketMessage(String senderEmail, String receiverEmail, String content) {
        MessageResponse tempMessage = MessageResponse.builder()
                .content(content)
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .build();
        
        // Send to receiver's private queue
        messagingTemplate.convertAndSendToUser(receiverEmail, "/queue/messages", tempMessage);
        log.info("WebSocket message sent from {} to {}", senderEmail, receiverEmail);
    }
    
    /**
     * Broadcast message to a room/group
     */
    public void broadcastMessage(String roomId, String senderEmail, String content) {
        MessageResponse message = MessageResponse.builder()
                .content(content)
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .build();
        
        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
        log.info("Message broadcasted to room {} by {}", roomId, senderEmail);
    }

    public void markMessagesAsRead(String senderEmail, String receiverEmail) {
        List<Message> messages = messageRepository.findUnreadMessages(senderEmail, receiverEmail);
        for (Message message : messages) {
            message.setRead(true);
        }
        messageRepository.saveAll(messages);
    }

    public List<MessageResponse> getMessagesBetweenUsers(String user1Email, String user2Email) {
        List<Message> messages = messageRepository.findMessagesBetweenUsers(user1Email, user2Email);
        return messages.stream().map(messageMapper::toMessageResponse).collect(Collectors.toList());
    }
}
