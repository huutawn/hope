package com.llt.hope.mapper;

import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;

import com.llt.hope.dto.response.PostResponse;
import com.llt.hope.entity.Post;
import com.llt.hope.repository.jpa.CommentRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {
    private final CommentRepository commentRepository;
   public PostResponse toPostResponse(Post post){
       return PostResponse.builder()
              .id(post.getId())
              .title(post.getTitle())
              .content(post.getContent())
               .userId(post.getUser().getId())
               .userPic(post.getUser().getProfile().getProfilePicture()!=null?post.getUser().getProfile().getProfilePicture().getUrl():null)
               .name(post.getUser().getProfile().getFullName()==null?post.getUser().getEmail():post.getUser().getProfile().getFullName())
               .isActive(post.isActive())
               .isPinned(post.isPinned())
               .type(post.getType())
               .isPublished(post.isPublished())
               .images(post.getImages())
               .createdAt(post.getCreatedAt())
               .commentCount(commentRepository.countByPostId(post.getId()))
               .like(post.getLikes())
              .build();
   }
}
