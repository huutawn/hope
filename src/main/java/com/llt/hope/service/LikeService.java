package com.llt.hope.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.llt.hope.dto.response.LikePostResponse;
import com.llt.hope.entity.*;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.repository.jpa.*;
import com.llt.hope.utils.SecurityUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LikeService {
    PostRepository postRepository;
    LikeRepository likeRepository;
    UserRepository userRepository;

    public LikePostResponse likeAction(long postId) {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        Like like = Like.builder()
                .post(post)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
        List<Like> likes = new ArrayList<>();
        likes.add(like);
        like = likeRepository.save(like);
        post.setLikes(likes);
        LikePostResponse likePostResponse = LikePostResponse.builder()
                .likeId(like.getId())
                .post(post)
                .user(user)
                .createdAt(like.getCreatedAt())
                .build();
        postRepository.save(post);
        return likePostResponse;
    }
}
