package com.llt.hope.mapper;

import com.llt.hope.dto.response.MessageBoxResponse;
import com.llt.hope.dto.response.MessageResponse;
import com.llt.hope.entity.Message;
import com.llt.hope.entity.MessageBox;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, MessageMapper.class})
public interface MessageBoxMapper {
    
    @Mapping(target = "receiver", source = "receiver")
    @Mapping(target = "lastMessage", expression = "java(getLastMessageContent(messageBox))")
    @Mapping(target = "recentMessages", expression = "java(getRecentMessages(messageBox))")
    MessageBoxResponse toMessageBoxResponse(MessageBox messageBox);

    List<MessageBoxResponse> toMessageBoxResponseList(List<MessageBox> messageBoxes);

    @Named("getLastMessageContent")
    default String getLastMessageContent(MessageBox messageBox) {
        if (messageBox.getMessages() == null || messageBox.getMessages().isEmpty()) {
            return null;
        }
        return messageBox.getMessages().get(messageBox.getMessages().size() - 1).getContent();
    }

    @Named("getRecentMessages")
    default List<MessageResponse> getRecentMessages(MessageBox messageBox) {
        if (messageBox.getMessages() == null || messageBox.getMessages().isEmpty()) {
            return List.of();
        }
        
        List<Message> messages = messageBox.getMessages();
        int size = messages.size();
        int startIndex = Math.max(0, size - 10); // Get last 10 messages
        
        return messages.subList(startIndex, size)
                .stream()
                .map(this::toMessageResponse)
                .toList();
    }

    // Helper method to convert Message to MessageResponse
    @Mapping(target = "sender", source = "sender")
    @Mapping(target = "receiver", source = "receiver")
    MessageResponse toMessageResponse(Message message);
}
