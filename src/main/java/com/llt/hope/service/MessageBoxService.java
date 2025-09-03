package com.llt.hope.service;

import com.llt.hope.dto.response.MessageBoxResponse;
import com.llt.hope.dto.response.MessageBoxUpdateNotification;
import com.llt.hope.dto.response.MessageResponse;
import com.llt.hope.entity.Message;
import com.llt.hope.entity.MessageBox;
import com.llt.hope.entity.MessageContainer;
import com.llt.hope.entity.User;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.mapper.MessageBoxMapper;
import com.llt.hope.mapper.MessageMapper;
import com.llt.hope.repository.jpa.MessageBoxRepository;
import com.llt.hope.repository.jpa.MessageContainerRepository;
import com.llt.hope.repository.jpa.MessageRepository;
import com.llt.hope.repository.jpa.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageBoxService {
    UserRepository userRepository;
    MessageRepository messageRepository;
    SimpMessagingTemplate messagingTemplate;
    MessageMapper messageMapper;
    MessageContainerRepository messageContainerRepository;
    MessageBoxRepository messageBoxRepository;
    MessageBoxMapper messageBoxMapper;

    /**
     * Get all message boxes for a user, ordered by last message time
     */
    public List<MessageBoxResponse> getAllMessageBox(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        List<MessageBox> messageBoxes = messageBoxRepository.findAllByUserOrderByLastMessageTimeDesc(user);
        return messageBoxMapper.toMessageBoxResponseList(messageBoxes);
    }

    /**
     * Get or create a message box for a specific receiver
     */
    @Transactional
    public MessageBoxResponse getOrCreateMessageBox(String currentUserEmail, String receiverEmail) {
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Optional<MessageBox> existingBox = messageBoxRepository.findMessageBoxByReceiver(receiver, currentUser);
        
        if (existingBox.isPresent()) {
            return messageBoxMapper.toMessageBoxResponse(existingBox.get());
        }

        // Create new message box
        MessageContainer container = getOrCreateMessageContainer(currentUser);
        
        MessageBox newMessageBox = MessageBox.builder()
                .receiver(receiver)
                .container(container)
                .messages(new ArrayList<>())
                .unreadCount(0)
                .build();

        MessageBox savedBox = messageBoxRepository.save(newMessageBox);
        return messageBoxMapper.toMessageBoxResponse(savedBox);
    }

    /**
     * Update message box when a new message is received (for real-time notifications)
     */
    @Transactional
    public void updateMessageBoxOnNewMessage(Message message) {
        User receiver = message.getReceiver();
        User sender = message.getSender();
        
        // Update receiver's message box
        MessageContainer receiverContainer = getOrCreateMessageContainer(receiver);
        Optional<MessageBox> receiverBox = messageBoxRepository.findMessageBoxByReceiver(sender, receiver);
        
        if (receiverBox.isEmpty()) {
            // Create new message box for receiver
            MessageBox newBox = MessageBox.builder()
                    .receiver(sender)
                    .container(receiverContainer)
                    .messages(new ArrayList<>())
                    .unreadCount(1)
                    .lastMessageTime(message.getSentAt())
                    .build();
            messageBoxRepository.save(newBox);
        } else {
            // Update existing message box
            MessageBox box = receiverBox.get();
            box.setUnreadCount(box.getUnreadCount() + 1);
            box.setLastMessageTime(message.getSentAt());
            messageBoxRepository.save(box);
        }
        
        // Send real-time notification to receiver about message box update
        notifyMessageBoxUpdate(receiver.getEmail());
    }

    /**
     * Mark messages as read in a message box
     */
    @Transactional
    public void markMessagesAsRead(String currentUserEmail, String senderEmail) {
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        Optional<MessageBox> messageBox = messageBoxRepository.findMessageBoxByReceiver(sender, currentUser);
        
        if (messageBox.isPresent()) {
            MessageBox box = messageBox.get();
            box.setUnreadCount(0);
            messageBoxRepository.save(box);
            
            // Notify real-time update
            notifyMessageBoxUpdate(currentUserEmail);
        }
    }

    /**
     * Get total unread message count for a user
     */
    public long getTotalUnreadCount(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        Long count = messageBoxRepository.getTotalUnreadCountByUser(user);
        return count != null ? count : 0;
    }

    /**
     * Get count of message boxes with unread messages
     */
    public long getUnreadMessageBoxCount(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        return messageBoxRepository.countUnreadMessageBoxesByUser(user);
    }

    /**
     * Send real-time notification about message box updates (like Facebook)
     */
    public void notifyMessageBoxUpdate(String userEmail) {
        try {
            List<MessageBoxResponse> messageBoxes = getAllMessageBox(userEmail);
            long totalUnread = getTotalUnreadCount(userEmail);
            
            MessageBoxUpdateNotification notification = MessageBoxUpdateNotification.builder()
                    .messageBoxes(messageBoxes)
                    .totalUnreadCount(totalUnread)
                    .timestamp(LocalDateTime.now())
                    .build();
            
            // Send to user's private queue for message box updates
            messagingTemplate.convertAndSendToUser(userEmail, "/queue/messagebox-updates", notification);
            
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
}
