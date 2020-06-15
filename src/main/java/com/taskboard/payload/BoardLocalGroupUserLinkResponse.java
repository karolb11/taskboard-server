package com.taskboard.payload;

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

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Long getLocalGroupId() {
        return localGroupId;
    }

    public void setLocalGroupId(Long localGroupId) {
        this.localGroupId = localGroupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
