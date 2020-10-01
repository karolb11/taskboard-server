package com.taskboard.payloadConverter;

import com.taskboard.model.ChatMessage;
import com.taskboard.payload.ChatMessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatMessageMapper {
    public static ChatMessageResponse chatMessageToChatMessageResponse(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .authorName(chatMessage.getAuthor().getName())
                .content(chatMessage.getContent())
                .date(chatMessage.getCreatedAt())
                .build();
    }
}
