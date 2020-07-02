package com.taskboard.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardLocalGroupUserLinkRequest {
    private Long boardId;
    private Long localRoleId;
    private String usernameOrEmail;

}
