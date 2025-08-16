package com.llt.hope.mapper;

import org.springframework.stereotype.Component;

import com.llt.hope.dto.response.CommentResponse;
import com.llt.hope.entity.Comment;

@Component
public class CommentMapper {

    public CommentResponse toCommentResponse(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentResponse.CommentResponseBuilder responseBuilder = CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt());

        // Add user information
        if (comment.getUser() != null) {
            responseBuilder
                    .userId(comment.getUser().getId())
                    .userEmail(comment.getUser().getEmail());
            
            // Add user name and profile picture if profile exists
            if (comment.getUser().getProfile() != null) {
                responseBuilder.userName(
                    comment.getUser().getProfile().getFullName() != null 
                        ? comment.getUser().getProfile().getFullName() 
                        : comment.getUser().getEmail()
                );
                
                if (comment.getUser().getProfile().getProfilePicture() != null) {
                    responseBuilder.userProfilePicture(
                        comment.getUser().getProfile().getProfilePicture().getUrl()
                    );
                }
            } else {
                responseBuilder.userName(comment.getUser().getEmail());
            }
        }

        // Add post information if this is a post comment
        if (comment.isPostComment()) {
            responseBuilder
                    .postId(comment.getPost().getId())
                    .postTitle(comment.getPost().getTitle())
                    .isPostComment(true)
                    .isPostVolunteerComment(false);
        }

        // Add post volunteer information if this is a post volunteer comment
        if (comment.isPostVolunteerComment()) {
            responseBuilder
                    .postVolunteerId(comment.getPostVolunteer().getId())
                    .postVolunteerTitle(comment.getPostVolunteer().getTitle())
                    .isPostComment(false)
                    .isPostVolunteerComment(true);
        }

        return responseBuilder.build();
    }
}
