package com.llt.hope.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageBoxResponse {
    Long id;
    UserResponse receiver;
    String lastMessage;
    LocalDateTime lastMessageTime;
    int unreadCount;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<MessageResponse> recentMessages;
}
