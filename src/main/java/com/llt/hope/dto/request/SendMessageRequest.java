package com.llt.hope.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMessageRequest {
    String senderEmail;
    String receiverEmail;
    String content;
}
