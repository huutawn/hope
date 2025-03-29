package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.response.MessageResponse;
import com.llt.hope.entity.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageResponse toMessageResponse(Message message);
}
