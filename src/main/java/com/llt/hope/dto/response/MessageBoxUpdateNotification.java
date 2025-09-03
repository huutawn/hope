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
public class MessageBoxUpdateNotification {
    List<MessageBoxResponse> messageBoxes;
    long totalUnreadCount;
    long unreadMessageBoxCount;
    LocalDateTime timestamp;
}
