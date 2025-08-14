package com.llt.hope.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.llt.hope.dto.response.NotificationResponse;
import com.llt.hope.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    
    @Mapping(source = "user.email", target = "userEmail")
    NotificationResponse toNotificationResponse(Notification notification);
}
