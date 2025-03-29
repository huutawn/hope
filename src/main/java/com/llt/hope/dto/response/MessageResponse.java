package com.llt.hope.dto.response;

import java.time.LocalDateTime;

import com.llt.hope.entity.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
    private Long id;

    private User sender;

    private User receiver;

    private String content;

    private boolean isRead;

    private LocalDateTime sentAt;
}
