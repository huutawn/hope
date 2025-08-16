package com.llt.hope.dto.response;

import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    
    Long id;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    
    // User information
    String userId;
    String userName;
    String userProfilePicture;
    String userEmail;
    
    // Post information (if comment is on a post)
    Long postId;
    String postTitle;
    
    // PostVolunteer information (if comment is on a post volunteer)
    Long postVolunteerId;
    String postVolunteerTitle;
    
    // Helper fields to identify comment type
    boolean isPostComment;
    boolean isPostVolunteerComment;
}
