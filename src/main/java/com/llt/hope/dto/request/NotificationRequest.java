package com.llt.hope.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {
    String userEmail;
    String title;
    String message;
    String type;
    String roleName; // For role-based notifications
}
