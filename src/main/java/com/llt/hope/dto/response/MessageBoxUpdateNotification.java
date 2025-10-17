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
public class MessageBoxUpdateNotification {
    List<MessageBoxResponse> messageBoxes;
    long totalUnreadCount;
    long unreadMessageBoxCount;
    LocalDateTime timestamp;
}
