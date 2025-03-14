package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.response.PostResponse;
import com.llt.hope.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
