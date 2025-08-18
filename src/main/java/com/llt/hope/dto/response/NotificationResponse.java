package com.llt.hope.dto.response;

import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    Long id;
    String userEmail;
    String title;
    String message;
    String type;
    boolean isRead;
    LocalDateTime createdAt;
}
