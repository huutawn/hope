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

        // Gửi tin nhắn qua WebSocket đến người nhận
        messagingTemplate.convertAndSendToUser(receiverEmail, "/queue/messages", message);

        return messageMapper.toMessageResponse(message);
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
