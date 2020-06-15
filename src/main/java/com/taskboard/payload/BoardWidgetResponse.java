package com.taskboard.payload;

import lombok.Data;

@Data
public class BoardWidgetResponse {
    private Long id;
    private String name;
    private String description;

    public BoardWidgetResponse() {

    }

    public BoardWidgetResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }


}
