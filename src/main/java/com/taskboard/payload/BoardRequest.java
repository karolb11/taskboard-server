package com.taskboard.payload;

import lombok.Data;

@Data
public class BoardRequest {
    String name;
    String description;

    public BoardRequest() {
    }

}
