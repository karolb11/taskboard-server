package com.taskboard.payload;

import lombok.Data;

@Data
public class BoardLocalGroupUserLinkResponse {
    private Long boardId;
    private Long localGroupId;
    private Long userId;

    public BoardLocalGroupUserLinkResponse() {
    }

    public BoardLocalGroupUserLinkResponse(Long boardId, Long localGroupId, Long userId) {
        this.boardId = boardId;
        this.localGroupId = localGroupId;
        this.userId = userId;
    }


}
