package com.llt.hope.mapper;

import com.llt.hope.dto.response.MessageResponse;
import com.llt.hope.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageResponse toMessageResponse(Message message);
}
