package com.llt.hope.mapper;

import com.llt.hope.dto.response.PostVolunteerResponse;
import com.llt.hope.entity.PostVolunteer;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostVolunteerMapper {
    private final UserMapper userMapper;
    public PostVolunteerResponse toPostVolunteerResponse(PostVolunteer postVolunteer){
        if(userMapper == null)
            return null;
        return PostVolunteerResponse.builder()
                .id(postVolunteer.getId())
                .title(postVolunteer.getTitle())
                .files(postVolunteer.getFiles())
                .fund(postVolunteer.getFund())
                .status(postVolunteer.getStatus())
                .user(userMapper.toUserResponse(postVolunteer.getUser()))
                .totalAmount(postVolunteer.getTotalAmount())
                .createAt(postVolunteer.getCreateAt())
                .content(postVolunteer.getContent())
                .location(postVolunteer.getLocation())
                .isActive(postVolunteer.isActive())
                .build();
    }
}
