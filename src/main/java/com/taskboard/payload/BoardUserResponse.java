package com.taskboard.payload;

import com.taskboard.model.LocalRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardUserResponse {
    private Long invitationId;
    private Long id;
    private String name;
    private LocalRole localRole;
    private boolean accepted;

    public BoardUserResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
