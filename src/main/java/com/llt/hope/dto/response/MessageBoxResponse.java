package com.llt.hope.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

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
