package com.llt.hope.mapper;

import org.springframework.stereotype.Component;

import com.llt.hope.dto.response.PostVolunteerResponse;
import com.llt.hope.entity.PostVolunteer;
import com.llt.hope.repository.jpa.CommentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostVolunteerMapper {
    private final UserMapper userMapper;
    private final CommentRepository commentRepository;

    public PostVolunteerResponse toPostVolunteerResponse(PostVolunteer postVolunteer) {
        if (userMapper == null) return null;
        return PostVolunteerResponse.builder()
                .id(postVolunteer.getId())
                .userId(postVolunteer.getUser().getId())
                .userPic(postVolunteer.getUser().getProfile().getProfilePicture()!=null?postVolunteer.getUser().getProfile().getProfilePicture().getUrl():null)
                .name(postVolunteer.getUser().getProfile().getFullName()==null?postVolunteer.getUser().getEmail():postVolunteer.getUser().getProfile().getFullName())
                .title(postVolunteer.getTitle())
                .files(postVolunteer.getFiles())
                .fund(postVolunteer.getFund())
                .status(postVolunteer.getStatus())
                .stk(postVolunteer.getStk())
                .bankName(postVolunteer.getBankName())
                .totalAmount(postVolunteer.getTotalAmount())
                .createAt(postVolunteer.getCreateAt())
                .content(postVolunteer.getContent())
                .location(postVolunteer.getLocation())
                .isActive(postVolunteer.isActive())
                .like(postVolunteer.getLike())
                .commentCount(commentRepository.countByPostVolunteerId(postVolunteer.getId()))
                .build();
    }
}
