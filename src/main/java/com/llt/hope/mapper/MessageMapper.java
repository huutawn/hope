package com.llt.hope.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.llt.hope.dto.response.MessageResponse;
import com.llt.hope.entity.Message;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {
    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "receiver", target = "receiver")
    MessageResponse toMessageResponse(Message message);
}
