package com.llt.hope.dto.response;

import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
    private Long id;

    private UserResponse sender;

    private UserResponse receiver;

    private String content;

    private boolean isRead;

    private LocalDateTime sentAt;
}
