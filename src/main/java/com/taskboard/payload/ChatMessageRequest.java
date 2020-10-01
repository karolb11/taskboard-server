package com.taskboard.payload;

import lombok.Data;

@Data
public class ChatMessageRequest {
    private Long boardId;
    private String content;
}
