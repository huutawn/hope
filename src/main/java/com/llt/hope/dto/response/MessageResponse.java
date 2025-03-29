package com.llt.hope.dto.response;

import com.llt.hope.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
