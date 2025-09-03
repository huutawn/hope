package com.llt.hope.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.llt.hope.entity.MessageBox;
import com.llt.hope.entity.MessageContainer;
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
    MessageContainerRepository messageContainerRepository;
    MessageBoxRepository messageBoxRepository;

    @Transactional
    public MessageResponse sendMessage(String senderEmail, String receiverEmail, String content) {
        // Validate users
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Create message
        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .build();

        // Handle message box for sender
        handleSenderMessageBox(sender, receiver, message);
        
        // Save message
        message = messageRepository.save(message);
        MessageResponse messageResponse = messageMapper.toMessageResponse(message);

        // Send real-time message to receiver
        log.info("Sending message to user: {} on topic: /queue/messages", receiverEmail);
        messagingTemplate.convertAndSendToUser(receiverEmail, "/queue/messages", messageResponse);
        
        // Handle receiver's message box for notifications
        handleReceiverMessageBox(sender, receiver, message);
        
        // Send message update to sender as well for their chat interface
        messagingTemplate.convertAndSendToUser(senderEmail, "/queue/messages", messageResponse);

        return messageResponse;
    }
    
    /**
     * Handle message box creation/update for sender
     */
    private void handleSenderMessageBox(User sender, User receiver, Message message) {
        try {
            MessageBox messageBox = messageBoxRepository.findMessageBoxByReceiver(receiver, sender)
                    .orElse(null);
            
            if (messageBox == null) {
                // Create new message box for sender
                MessageContainer senderContainer = getOrCreateMessageContainer(sender);
                
                List<Message> messages = new ArrayList<>();
                messages.add(message);
                
                messageBox = MessageBox.builder()
                        .messages(messages)
                        .container(senderContainer)
                        .receiver(receiver)
                        .lastMessageTime(message.getSentAt())
                        .unreadCount(0) // Sender doesn't have unread count for their own messages
                        .build();
            } else {
                messageBox.getMessages().add(message);
                messageBox.setLastMessageTime(message.getSentAt());
            }

            messageBoxRepository.save(messageBox);
        } catch (Exception e) {
            log.error("Error handling sender message box for sender: {} and receiver: {}", 
                    sender.getEmail(), receiver.getEmail(), e);
        }
    }
    
    /**
     * Handle message box creation/update for receiver (for notifications)
     */
    private void handleReceiverMessageBox(User sender, User receiver, Message message) {
        try {
            MessageContainer receiverContainer = getOrCreateMessageContainer(receiver);
            MessageBox receiverBox = messageBoxRepository.findMessageBoxByReceiver(sender, receiver)
                    .orElse(null);
            
            if (receiverBox == null) {
                // Create new message box for receiver
                receiverBox = MessageBox.builder()
                        .receiver(sender)
                        .container(receiverContainer)
                        .messages(new ArrayList<>())
                        .unreadCount(1)
                        .lastMessageTime(message.getSentAt())
                        .build();
            } else {
                // Update existing message box
                receiverBox.setUnreadCount(receiverBox.getUnreadCount() + 1);
                receiverBox.setLastMessageTime(message.getSentAt());
            }
            
            messageBoxRepository.save(receiverBox);
            
            sendMessageBoxUpdateNotification(receiver.getEmail());
            
        } catch (Exception e) {
            log.error("Error handling receiver message box for sender: {} and receiver: {}", 
                    sender.getEmail(), receiver.getEmail(), e);
        }
    }
    
    /**
     * Send message box update notification (simplified version)
     */
    private void sendMessageBoxUpdateNotification(String userEmail) {
        try {
            // Send a simple notification to trigger message box refresh on frontend
            messagingTemplate.convertAndSendToUser(userEmail, "/queue/messagebox-updates", 
                    "MESSAGE_BOX_UPDATE");
            
            log.info("Message box update notification sent to user: {}", userEmail);
        } catch (Exception e) {
            log.error("Error sending message box update notification to user: {}", userEmail, e);
        }
    }
    
    /**
     * Get or create message container for user
     */
    private MessageContainer getOrCreateMessageContainer(User user) {
        if (user.getMessageContainer() == null) {
            MessageContainer container = MessageContainer.builder()
                    .user(user)
                    .messageBoxes(new ArrayList<>())
                    .build();
            return messageContainerRepository.save(container);
        }
        return user.getMessageContainer();
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
