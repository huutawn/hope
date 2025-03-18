package com.llt.hope.dto.response;

import java.time.LocalDateTime;

import com.llt.hope.entity.Post;
import com.llt.hope.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikePostResponse {
    private long likeId;
    private User user;
    private Post post;
    private LocalDateTime createdAt;
}
