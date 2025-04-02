package com.llt.hope.mapper;

import com.llt.hope.dto.response.SupportResponse;
import com.llt.hope.entity.Support;
import org.springframework.stereotype.Component;

@Component
public class SupportMapper {
    public SupportResponse toResponse(Support support) {
        return SupportResponse.builder()
                .id(support.getId())
                .userId(support.getUser().getId())
                .userName(support.getUser().getProfile().getFullName())
                .userEmail(support.getUser().getEmail())
                .postId(support.getPostVolunteer().getId())
                .postTitle(support.getPostVolunteer().getTitle())
                .donatedMoney(support.getDonatedMoney())
                .donatedAt(support.getDonatedAt())
                .build();
    }
}
