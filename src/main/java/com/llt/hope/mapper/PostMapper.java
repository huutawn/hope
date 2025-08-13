package com.llt.hope.mapper;

import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;

import com.llt.hope.dto.response.PostResponse;
import com.llt.hope.entity.Post;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {
    private final UserMapper userMapper;
   public PostResponse toPostResponse(Post post){
       return PostResponse.builder()
              .id(post.getId())
              .title(post.getTitle())
              .content(post.getContent())
              .user(userMapper.toUserResponse(post.getUser()))
               .isActive(post.isActive())
               .isPinned(post.isPinned())
               .type(post.getType())
               .isPublished(post.isPublished())
               .images(post.getImages())
               .createdAt(post.getCreatedAt())
              .build();
   }
}
