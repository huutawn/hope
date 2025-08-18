package com.llt.hope.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreationRequest {
    
    @NotBlank(message = "Comment content cannot be blank")
    String content;
    
    // Either postId or postVolunteerId should be provided, but not both
    Long postId;
    
    Long postVolunteerId;
}
